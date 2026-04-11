package br.com.mbausp.eda.product.oferta.domain.conta;

import java.time.LocalDateTime;

import br.com.mbausp.eda.product.oferta.domain.Constants;

public record ContaClienteItem(Integer id, Long number, String idCliente, LocalDateTime dataCriacao, ContaStatus status) {

	public Integer getOfertaId() {
		return Constants.ORIGEM_OFERTA_CONTA_ID;
	}

	public Integer getOrigemOferta() {
		return Constants.ORIGEM_OFERTA_CONTA_ID;
	}
}