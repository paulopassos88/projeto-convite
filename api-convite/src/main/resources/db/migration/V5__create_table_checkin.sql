CREATE TABLE checkin (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    convite_id UUID NOT NULL,
    evento_id UUID NOT NULL,
    controlador_id UUID NOT NULL,
    data_hora TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    tipo_entrada VARCHAR(50) NOT NULL,
    resultado VARCHAR(50) NOT NULL,
    motivo_negativa VARCHAR(255),
    dispositivo VARCHAR(255),
    CONSTRAINT fk_checkin_convite FOREIGN KEY (convite_id) REFERENCES convite (id),
    CONSTRAINT fk_checkin_evento FOREIGN KEY (evento_id) REFERENCES evento (id),
    CONSTRAINT fk_checkin_controlador FOREIGN KEY (controlador_id) REFERENCES usuario (id)
);
