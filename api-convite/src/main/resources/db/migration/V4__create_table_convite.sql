CREATE TABLE convite (
    id UUID PRIMARY KEY,
    convidado_id UUID NOT NULL,
    evento_id UUID NOT NULL,
    codigo VARCHAR(255) NOT NULL UNIQUE,
    qr_code_url VARCHAR(255),
    status VARCHAR(50) NOT NULL,
    enviado_em TIMESTAMP,
    status_envio VARCHAR(50),
    criado_em TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    atualizado_em TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_convite_convidado FOREIGN KEY (convidado_id) REFERENCES convidado (id),
    CONSTRAINT fk_convite_evento FOREIGN KEY (evento_id) REFERENCES evento (id)
);
