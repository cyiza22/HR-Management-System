CREATE TABLE password_reset_token
(
    id          BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    token       VARCHAR(255),
    user_id     BIGINT                                  NOT NULL,
    expiry_date TIMESTAMP WITHOUT TIME ZONE,
    CONSTRAINT pk_passwordresettoken PRIMARY KEY (id)
);

ALTER TABLE employees
    ADD bank_account_number VARCHAR(255);

ALTER TABLE employees
    ADD linked_in VARCHAR(255);

ALTER TABLE users
    ADD otp_generated_time TIMESTAMP WITHOUT TIME ZONE;

ALTER TABLE password_reset_token
    ADD CONSTRAINT uc_passwordresettoken_user UNIQUE (user_id);

ALTER TABLE password_reset_token
    ADD CONSTRAINT FK_PASSWORDRESETTOKEN_ON_USER FOREIGN KEY (user_id) REFERENCES users (id);

ALTER TABLE payroll
ALTER
COLUMN ctc TYPE DECIMAL(12, 2) USING (ctc::DECIMAL(12, 2));

ALTER TABLE payroll
ALTER
COLUMN deduction TYPE DECIMAL(12, 2) USING (deduction::DECIMAL(12, 2));

ALTER TABLE users
    ALTER COLUMN is_verified DROP NOT NULL;

ALTER TABLE payroll
ALTER
COLUMN salary_per_month TYPE DECIMAL(12, 2) USING (salary_per_month::DECIMAL(12, 2));