package br.com.mbausp.eda.product.conta.domain;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ContaClientePayload(@NotNull Long number, @NotBlank String idCliente) {

}
