package br.com.mbausp.eda.evento.domain.auto;

import java.time.LocalDateTime;

import br.com.mbausp.eda.evento.domain.Constants;

public record AutoClienteItem(Integer id, Integer contractNumber, String idCliente, LocalDateTime dataCriacao, AutoStatus status) {

	public Integer getOfertaId() {
		return Constants.ORIGEM_OFERTA_AUTO_ID;
	}

	public Integer getOrigemOferta() {
		return Constants.ORIGEM_OFERTA_AUTO_ID;
	}
}