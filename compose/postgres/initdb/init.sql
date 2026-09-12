-- Criar múltiplos databases
CREATE DATABASE db_auto;
CREATE DATABASE db_conta;
CREATE DATABASE db_oferta;

-- (opcional) criar usuários
CREATE USER userauto WITH PASSWORD '123456';
CREATE USER userconta WITH PASSWORD '123456';
CREATE USER useroferta WITH PASSWORD '123456';

-- criar tabela
\connect db_auto;

CREATE SCHEMA IF NOT EXISTS auto AUTHORIZATION userauto;
    
CREATE TABLE IF NOT EXISTS auto.auto_cliente
(
    id integer NOT NULL GENERATED ALWAYS AS IDENTITY ( INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1 ) PRIMARY KEY,
    cliente_id character varying(15) COLLATE pg_catalog."default" NOT NULL,
    nu_contrato integer NOT NULL,
    status character varying(15) COLLATE pg_catalog."default" NOT NULL,
    data_criacao timestamp with time zone NOT NULL,
    CONSTRAINT uk_auto_cliente UNIQUE (cliente_id, nu_contrato)
)

TABLESPACE pg_default;

-- criar tabela
\connect db_conta;

CREATE SCHEMA IF NOT EXISTS conta AUTHORIZATION userconta;
    
CREATE TABLE IF NOT EXISTS conta.conta_cliente
(
    id integer NOT NULL GENERATED ALWAYS AS IDENTITY ( INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1 ) PRIMARY KEY,
    cliente_id character varying(15) COLLATE pg_catalog."default" NOT NULL,
    nu_conta integer NOT NULL,
    status character varying(15) COLLATE pg_catalog."default" NOT NULL,
    data_criacao timestamp with time zone NOT NULL,
    CONSTRAINT uk_conta_usuario UNIQUE (cliente_id, nu_conta)
)

TABLESPACE pg_default;

-- criar tabela
\connect db_oferta;

CREATE SCHEMA IF NOT EXISTS oferta AUTHORIZATION useroferta;
    
CREATE TABLE IF NOT EXISTS oferta.oferta_cliente
(
    id integer NOT NULL GENERATED ALWAYS AS IDENTITY ( INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1 ) PRIMARY KEY,
    cliente_id character varying(15) COLLATE pg_catalog."default" NOT NULL,
    oferta_id integer NOT NULL,
    oferta_ativa boolean NOT NULL,
    data_criacao timestamp with time zone NOT NULL,
    data_expiracao timestamp with time zone NOT NULL,
    status character varying(15) COLLATE pg_catalog."default" NOT NULL,
    origem_oferta integer NOT NULL,
    CONSTRAINT uk_cliente_oferta UNIQUE (cliente_id, oferta_id)
)

TABLESPACE pg_default;

-- (opcional) permissões
GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA auto TO userauto;
GRANT ALL PRIVILEGES ON ALL SEQUENCES IN SCHEMA auto TO userauto;
GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA conta TO userconta;
GRANT ALL PRIVILEGES ON ALL SEQUENCES IN SCHEMA conta TO userconta;
GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA oferta TO useroferta;
GRANT ALL PRIVILEGES ON ALL SEQUENCES IN SCHEMA oferta TO useroferta;
