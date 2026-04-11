package br.com.mbausp.eda.evento.domain;

import java.time.LocalDateTime;

public record ParsedMessage(String idCliente, Integer idOferta, LocalDateTime dataCriacao, Integer origemOferta) {}