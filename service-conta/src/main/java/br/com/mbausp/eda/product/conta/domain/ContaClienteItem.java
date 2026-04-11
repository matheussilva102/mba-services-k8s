package br.com.mbausp.eda.product.conta.domain;

import java.time.LocalDateTime;

public record ContaClienteItem(Integer id, Long number, String idCliente, LocalDateTime dataCriacao, ContaStatus status) {

}
