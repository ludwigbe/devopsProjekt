CREATE TABLE consultant
(
    id BIGINT NOT NULL,
    CONSTRAINT pk_consultant PRIMARY KEY (id)
);
CREATE SEQUENCE IF NOT EXISTS customers_seq START WITH 1 INCREMENT BY 50;

CREATE TABLE customers
(
    id            BIGINT NOT NULL,
    name          VARCHAR(255),
    city          VARCHAR(255),
    consultant_id BIGINT,
    CONSTRAINT pk_customers PRIMARY KEY (id)
);

ALTER TABLE customers
    ADD CONSTRAINT FK_CUSTOMERS_ON_CONSULTANT FOREIGN KEY (consultant_id) REFERENCES consultant (id);
DROP SEQUENCE consultants_project_seq CASCADE;

DROP SEQUENCE consultants_seq CASCADE;

DROP SEQUENCE customer_seq CASCADE;

DROP SEQUENCE customers_seq CASCADE;

DROP SEQUENCE entries_seq CASCADE;

DROP SEQUENCE projects_seq CASCADE;

DROP SEQUENCE user_consultant_mapping_seq CASCADE;

DROP SEQUENCE user_mapping_seq CASCADE;

DROP SEQUENCE usermapping_seq CASCADE;

DROP SEQUENCE usermappings_seq CASCADE; 