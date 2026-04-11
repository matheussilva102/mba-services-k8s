package br.com.mbausp.eda.product.oferta.domain;

import java.time.LocalDateTime;

public record ParsedMessageIn(String idCliente, Integer idOferta, LocalDateTime dataCriacao, Integer origemOferta) {}