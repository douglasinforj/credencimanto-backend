-- EVENTOS
CREATE TABLE events (
    id              UUID         PRIMARY KEY DEFAULT gen_random_uuid(),
    name            VARCHAR(200) NOT NULL,
    description     VARCHAR(1000),
    start_date      TIMESTAMP    NOT NULL,
    end_date        TIMESTAMP    NOT NULL,
    location        VARCHAR(300) NOT NULL,
    max_capacity    INTEGER,
    status          VARCHAR(20)  NOT NULL DEFAULT 'DRAFT',
    badge_template  VARCHAR(50)           DEFAULT 'DEFAULT',
    created_at      TIMESTAMP             DEFAULT now(),
    updated_at      TIMESTAMP             DEFAULT now()
);

-- USUÁRIOS DO SISTEMA
CREATE TABLE system_users (
    id              UUID         PRIMARY KEY DEFAULT gen_random_uuid(),
    name            VARCHAR(150) NOT NULL,
    email           VARCHAR(200) NOT NULL UNIQUE,
    password        VARCHAR(255) NOT NULL,
    role            VARCHAR(20)  NOT NULL,
    document_type   VARCHAR(20),
    document_number VARCHAR(30),
    active          BOOLEAN      NOT NULL DEFAULT true,
    created_at      TIMESTAMP             DEFAULT now()
);

-- PARTICIPANTS
CREATE TABLE participants (
    id              UUID         PRIMARY KEY DEFAULT gen_random_uuid(),
    name            VARCHAR(200) NOT NULL,
    email           VARCHAR(200) NOT NULL,
    phone           VARCHAR(20),
    organization    VARCHAR(200),
    type            VARCHAR(20)  NOT NULL DEFAULT 'PARTICIPANT',
    document_type   VARCHAR(20),
    document_number VARCHAR(30),
    photo_path      VARCHAR(500),
    qr_code_path    VARCHAR(500),
    qr_code_token   VARCHAR(100) NOT NULL UNIQUE,
    checked_in      BOOLEAN      NOT NULL DEFAULT false,
    checked_in_at   TIMESTAMP,
    event_id        UUID         NOT NULL REFERENCES events(id) ON DELETE CASCADE,
    created_at      TIMESTAMP             DEFAULT now(),
    updated_at      TIMESTAMP             DEFAULT now(),

    -- E-mail único por evento
    CONSTRAINT uk_participant_email_event UNIQUE (email, event_id),

    -- Documento único por evento (tipo + número + evento)
    CONSTRAINT uk_participant_document_event UNIQUE (document_type, document_number, event_id)
);



-- ÍNDICES DE PERFORMANCE
CREATE INDEX idx_participants_event_id    ON participants(event_id);
CREATE INDEX idx_participants_qr_token    ON participants(qr_code_token);
CREATE INDEX idx_participants_email       ON participants(email);
CREATE INDEX idx_participants_type        ON participants(type);
CREATE INDEX idx_participants_checked_in  ON participants(checked_in);
CREATE INDEX idx_participants_document    ON participants(document_type, document_number);
CREATE INDEX idx_system_users_email       ON system_users(email);

-- ADMIN PADRÃO (senha: Admin@123 - trocar no primeiro login)
INSERT INTO system_users (id, name, email, password, role)
VALUES (
    gen_random_uuid(),
    'Administrador',
    'admin@credenciamento.com',
    '$2a$12$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi',
    'ADMIN'
);