CREATE TABLE autorizacao_manual (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    evento_id UUID NOT NULL,
    convite_id UUID,
    organizador_id UUID NOT NULL,
    controlador_id UUID,
    motivo VARCHAR(255) NOT NULL,
    criado_em TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_autorizacao_evento FOREIGN KEY (evento_id) REFERENCES evento (id),
    CONSTRAINT fk_autorizacao_convite FOREIGN KEY (convite_id) REFERENCES convite (id),
    CONSTRAINT fk_autorizacao_organizador FOREIGN KEY (organizador_id) REFERENCES usuario (id),
    CONSTRAINT fk_autorizacao_controlador FOREIGN KEY (controlador_id) REFERENCES usuario (id)
);
