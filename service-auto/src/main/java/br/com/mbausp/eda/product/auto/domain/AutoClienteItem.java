package br.com.mbausp.eda.product.auto.domain;

import java.time.LocalDateTime;

public record AutoClienteItem(Integer id, Integer contractNumber, String idCliente, LocalDateTime dataCriacao, AutoStatus status) {

}