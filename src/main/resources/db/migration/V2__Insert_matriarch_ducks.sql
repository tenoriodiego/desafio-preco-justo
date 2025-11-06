-- Migration: Inserção de Patos Matriarcas (Mães)
-- Estas serão as patos mães iniciais do sistema

INSERT INTO pato (nome, preco_categ, vendido, created_at, filho_count, mae_id) 
VALUES 
    ('Dona Patilda', 'PREMIUM', false, NOW(), 0, NULL),
    ('Mãe Patalina', 'STANDARD', false, NOW(), 0, NULL),
    ('Matriarca Penelope', 'PREMIUM', false, NOW(), 0, NULL),
    ('Rainha Quackmira', 'LUXO', false, NOW(), 0, NULL),
    ('Madame Pata', 'STANDARD', false, NOW(), 0, NULL),
    ('Senhora dos Lagos', 'PREMIUM', false, NOW(), 0, NULL),
    ('Dama das Penas', 'LUXO', false, NOW(), 0, NULL),
    ('Avó Patagônia', 'STANDARD', false, NOW(), 0, NULL)
ON CONFLICT DO NOTHING;

-- Opcional: Inserir alguns patos filhos para demonstração
INSERT INTO pato (nome, preco_categ, vendido, created_at, filho_count, mae_id) 
SELECT 
    'Filho da ' || mae.nome,
    CASE 
        WHEN mae.preco_categ = 'LUXO' THEN 'PREMIUM'
        WHEN mae.preco_categ = 'PREMIUM' THEN 'STANDARD' 
        ELSE 'BASIC'
    END,
    false,
    NOW(),
    0,
    mae.id
FROM pato mae 
WHERE mae.mae_id IS NULL 
AND mae.nome IN ('Dona Patilda', 'Mãe Patalina', 'Rainha Quackmira')
LIMIT 6
ON CONFLICT DO NOTHING;