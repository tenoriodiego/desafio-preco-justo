package br.com.precojusto.desafio_granja_patos.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.precojusto.desafio_granja_patos.entity.Pato;
import br.com.precojusto.desafio_granja_patos.entity.Venda;
import br.com.precojusto.desafio_granja_patos.entity.VendaPato;
import br.com.precojusto.desafio_granja_patos.repository.PatoRepository;
import br.com.precojusto.desafio_granja_patos.repository.VendaRepository;

@Service
public class RelatorioVendaService {

    private final PatoRepository patoRepository;
    private final VendaRepository vendaRepository;

    public RelatorioVendaService(PatoRepository patoRepository, VendaRepository vendaRepository) {
        this.patoRepository = patoRepository;
        this.vendaRepository = vendaRepository;
    }

    @Transactional(readOnly = true)
    public byte[] gerarRelatorio(LocalDate dataInicial, LocalDate dataFinal) throws IOException {

        // Define o período
        OffsetDateTime inicio = (dataInicial == null)
                ? OffsetDateTime.of(LocalDate.of(1970, 1, 1).atStartOfDay(), ZoneOffset.UTC)
                : dataInicial.atStartOfDay(ZoneId.systemDefault()).toOffsetDateTime();

        OffsetDateTime fim = (dataFinal == null)
                ? OffsetDateTime.now()
                : dataFinal.atTime(LocalTime.MAX).atZone(ZoneId.systemDefault()).toOffsetDateTime();

        // SOLUÇÃO DEFINITIVA: Busca apenas os dados necessários em formato de DTO
        List<VendaPato> todosItens = vendaRepository.findItensComPatoPorPeriodo(inicio, fim);

        // Cria mapa direto de patoId -> VendaPato (sem manipular entidades JPA)
        Map<Long, VendaPato> vendaPorPato = todosItens.stream()
                .filter(vp -> vp.getPato() != null && vp.getPato().getId() != null)
                .collect(Collectors.toMap(
                        vp -> vp.getPato().getId(),
                        vp -> vp,
                        (existing, replacement) -> existing));

        // Busca todos os patos
        List<Pato> todos = patoRepository.findAll();

        // Agrupa filhos por mãe
        Map<Long, List<Pato>> filhosMap = todos.stream()
                .filter(p -> p.getMae() != null && p.getMae().getId() != null)
                .collect(Collectors.groupingBy(p -> p.getMae().getId()));

        // Matriarcas (sem mãe)
        List<Pato> matriarcas = patoRepository.findByMaeIsNull();
        matriarcas.sort(Comparator.comparing(Pato::getNome));

        // ======== GERAÇÃO DO EXCEL (MANTIDO IGUAL) ========

        try (SXSSFWorkbook wb = new SXSSFWorkbook(100);
                ByteArrayOutputStream bos = new ByteArrayOutputStream()) {

            wb.setCompressTempFiles(true);

            SXSSFSheet sheet = (SXSSFSheet) wb.createSheet("RELATÓRIO DE VENDA");
            sheet.trackAllColumnsForAutoSizing();

            // ======= Estilos =======
            CellStyle titleStyle = wb.createCellStyle();
            Font titleFont = wb.createFont();
            titleFont.setBold(true);
            titleFont.setFontHeightInPoints((short) 14);
            titleStyle.setFont(titleFont);

            CellStyle headerStyle = wb.createCellStyle();
            Font headerFont = wb.createFont();
            headerFont.setBold(true);
            headerStyle.setFont(headerFont);
            headerStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
            headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            headerStyle.setAlignment(HorizontalAlignment.CENTER);

            DataFormat df = wb.createDataFormat();

            CellStyle defaultStyle = wb.createCellStyle();
            defaultStyle.setDataFormat(df.getFormat("text"));

            CellStyle moneyStyle = wb.createCellStyle();
            moneyStyle.setDataFormat(df.getFormat("R$ #,##0.00"));

            CellStyle dateStyle = wb.createCellStyle();
            dateStyle.setDataFormat(df.getFormat("dd/MM/yyyy HH:mm"));

            // ======= Cabeçalhos do relatório =======
            Row r0 = sheet.createRow(0);
            Cell c0 = r0.createCell(0);
            c0.setCellValue("RELATÓRIO DE VENDA");
            c0.setCellStyle(titleStyle);

            Row r1 = sheet.createRow(1);
            r1.createCell(0).setCellValue("Gerado em:");
            r1.createCell(1).setCellValue(OffsetDateTime.now()
                    .format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")));

            Row r2 = sheet.createRow(2);
            r2.createCell(0).setCellValue("Data inicial:");
            r2.createCell(1).setCellValue(
                    dataInicial == null ? "-" : dataInicial.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
            r2.createCell(2).setCellValue("Data final:");
            r2.createCell(3).setCellValue(
                    dataFinal == null ? "-" : dataFinal.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));

            // ======= Cabeçalho das colunas =======
            String[] cols = { "Nome", "Status", "Cliente", "Tipo do Cliente", "Valor", "Data/hora", "Vendedor" };
            Row header = sheet.createRow(4);
            for (int i = 0; i < cols.length; i++) {
                Cell cell = header.createCell(i);
                cell.setCellValue(cols[i]);
                cell.setCellStyle(headerStyle);
            }

            AtomicInteger rowIndex = new AtomicInteger(5);

            // ======= Escrita recursiva dos patos =======
            class Writer {
                void write(Pato p, int nivel) {
                    Row r = sheet.createRow(rowIndex.getAndIncrement());

                    CellStyle indentStyle = wb.createCellStyle();
                    indentStyle.cloneStyleFrom(defaultStyle);
                    indentStyle.setIndention((short) nivel);

                    // Nome (com indentação)
                    Cell nomeCell = r.createCell(0);
                    nomeCell.setCellValue(p.getNome());
                    nomeCell.setCellStyle(indentStyle);

                    // Status
                    r.createCell(1).setCellValue(p.isVendido() ? "Vendido" : "Disponível");

                    // Informações de venda (se houver)
                    VendaPato vp = vendaPorPato.get(p.getId());
                    if (vp != null && vp.getVenda() != null) {
                        Venda venda = vp.getVenda();

                        r.createCell(2).setCellValue(venda.getCliente() != null ? venda.getCliente().getNome() : "-");
                        r.createCell(3).setCellValue(
                                (venda.getCliente() != null && venda.getCliente().isElegivelDesconto())
                                        ? "Com Desconto"
                                        : "Sem Desconto");

                        Cell valorCell = r.createCell(4);
                        valorCell.setCellValue(
                                vp.getPrecoUnitario() != null ? vp.getPrecoUnitario().doubleValue() : 0.0);
                        valorCell.setCellStyle(moneyStyle);

                        Cell dataCell = r.createCell(5);
                        if (venda.getDataVenda() != null) {
                            dataCell.setCellValue(Date.from(venda.getDataVenda().toInstant()));
                            dataCell.setCellStyle(dateStyle);
                        } else {
                            dataCell.setCellValue("-");
                        }

                        r.createCell(6).setCellValue(
                                venda.getVendedor() != null ? venda.getVendedor().getNome() : "-");
                    } else {
                        for (int i = 2; i <= 6; i++)
                            r.createCell(i).setCellValue("-");
                    }

                    // Filhos
                    List<Pato> filhos = new ArrayList<>(filhosMap.getOrDefault(p.getId(), Collections.emptyList()));
                    filhos.sort(Comparator.comparing(Pato::getNome));
                    for (Pato f : filhos)
                        write(f, nivel + 1);
                }
            }

            Writer writer = new Writer();
            for (Pato m : matriarcas)
                writer.write(m, 0);

            // ======= Ajusta largura =======
            for (int i = 0; i < cols.length; i++) {
                try {
                    sheet.autoSizeColumn(i);
                    int width = sheet.getColumnWidth(i);
                    if (width > 10000)
                        sheet.setColumnWidth(i, 10000);
                } catch (IllegalStateException e) {
                    // Ignora colunas não rastreadas
                }
            }

            wb.write(bos);
            wb.dispose();
            return bos.toByteArray();
        }
    }
}