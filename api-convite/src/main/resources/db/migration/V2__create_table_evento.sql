CREATE TABLE evento (
    id UUID PRIMARY KEY,
    organizador_id UUID NOT NULL,
    nome VARCHAR(255) NOT NULL,
    descricao TEXT,
    local_nome VARCHAR(255) NOT NULL,
    endereco VARCHAR(255) NOT NULL,
    link_maps VARCHAR(255),
    data_inicio TIMESTAMP NOT NULL,
    data_termino TIMESTAMP NOT NULL,
    antecedencia_minutos INT NOT NULL,
    tolerancia_atraso_minutos INT NOT NULL,
    acompanhantes_padrao INT NOT NULL,
    status VARCHAR(50) NOT NULL,
    criado_em TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    atualizado_em TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_evento_organizador FOREIGN KEY (organizador_id) REFERENCES usuario (id)
);
