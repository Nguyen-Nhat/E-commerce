--liquibase formatted sql
--changeset ckxnhat:init-identity-data

INSERT INTO role(name)
VALUES('ADMIN'), ('USER');

INSERT INTO user (role_id, full_name, phone_number, email, password, dob, address, is_deleted) VALUES
(2,'user', '1234567890','user@example.com', '$2a$10$xKUZWQyXIHlP8F6O2JBoVOyDjBQSuDsqD3ZpBC0xfxWLhMW0Bjcie', '1980-01-01','123 Main St, Anytown, USA',FALSE),
(1,'admin', '1234567890','admin@example.com', '$2a$10$xKUZWQyXIHlP8F6O2JBoVOyDjBQSuDsqD3ZpBC0xfxWLhMW0Bjcie', '1980-01-01','123 Main St, Anytown, USA',FALSE);
