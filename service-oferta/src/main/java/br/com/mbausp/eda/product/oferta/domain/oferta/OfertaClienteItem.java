package br.com.mbausp.eda.product.oferta.domain.oferta;

import java.time.LocalDateTime;

public record OfertaClienteItem(
		Integer id,
		String idCliente,
		Integer idOferta,
		boolean ofertaAtiva,
		LocalDateTime dataCriacao,
		LocalDateTime dataExpiracao,
		String status,
		Integer origemOferta) {

}
