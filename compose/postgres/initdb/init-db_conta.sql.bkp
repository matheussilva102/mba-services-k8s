-- Criar múltiplos databases
CREATE DATABASE db_conta;

-- (opcional) criar usuários
CREATE USER userconta WITH PASSWORD '123456';

-- criar tabela
\connect db_conta;

CREATE SCHEMA IF NOT EXISTS conta AUTHORIZATION userconta;
    
CREATE TABLE IF NOT EXISTS conta.conta_cliente
(
    id integer NOT NULL GENERATED ALWAYS AS IDENTITY ( INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1 ),
    cliente_id character varying(15) COLLATE pg_catalog."default" NOT NULL,
    nu_conta integer NOT NULL,
    digito_conta character(2) COLLATE pg_catalog."default" NOT NULL,
    conta_ativa boolean NOT NULL,
    status character varying(15) COLLATE pg_catalog."default" NOT NULL,
    data_criacao timestamp with time zone NOT NULL,
    data_alteracao timestamp with time zone,
    CONSTRAINT conta_usuario_pk PRIMARY KEY (cliente_id, nu_conta)
)

TABLESPACE pg_default;

-- (opcional) permissões
GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA conta TO userconta;
GRANT ALL PRIVILEGES ON ALL SEQUENCES IN SCHEMA conta TO userconta;
