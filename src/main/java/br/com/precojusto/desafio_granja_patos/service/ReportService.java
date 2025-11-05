package br.com.precojusto.desafio_granja_patos.service;

import java.io.OutputStream;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.stereotype.Service;

import br.com.precojusto.desafio_granja_patos.repository.PatoRepository;
import br.com.precojusto.desafio_granja_patos.repository.VendaRepository;
import jakarta.servlet.http.HttpServletResponse;

@Service
public class ReportService {
    private final PatoRepository patoRepository;
    private final VendaRepository vendaRepository;

    public ReportService(PatoRepository patoRepository, VendaRepository vendaRepository) {
        this.patoRepository = patoRepository;
        this.vendaRepository = vendaRepository;
    }

    public void exportPatosCadastrados(HttpServletResponse response) throws Exception {
        try (SXSSFWorkbook wb = new SXSSFWorkbook(); OutputStream os = response.getOutputStream()) {
            Sheet s = wb.createSheet("Patos");
            Row header = s.createRow(0);
            header.createCell(0).setCellValue("ID");
            header.createCell(1).setCellValue("Nome");
            header.createCell(2).setCellValue("MaeId");
            header.createCell(3).setCellValue("Filhos");
            header.createCell(4).setCellValue("Vendido");

            int r = 1;
            for (var p : patoRepository.findAll()) {
                Row row = s.createRow(r++);
                row.createCell(0).setCellValue(p.getId());
                row.createCell(1).setCellValue(p.getNome());
                row.createCell(2).setCellValue(p.getMae() == null ? "" : String.valueOf(p.getMae().getId()));
                row.createCell(3).setCellValue(p.getFilhoCount() == null ? 0 : p.getFilhoCount());
                row.createCell(4).setCellValue(p.isVendido());
            }

            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setHeader("Content-Disposition", "attachment; filename=patos_cadastrados.xlsx");
            wb.write(os);
            os.flush();
        }
    }
}