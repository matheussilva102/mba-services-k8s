package br.com.mbausp.eda.product.oferta.domain;

import java.time.LocalDateTime;

import br.com.mbausp.eda.product.oferta.utils.ObjectMapperUtils;

public class ParsedMessage {

	private String idCliente;

	private Integer idOferta;

	private LocalDateTime dataCriacao;

	private LocalDateTime dataExpiracao;

	private Integer origemOferta;

	private String status;

	private boolean ofertaAtiva;

	public ParsedMessage(
			String idCliente,
			Integer idOferta,
			LocalDateTime dataCriacao,
			LocalDateTime dataExpiracao,
			Integer origemOferta,
			String status,
			boolean ofertaAtiva) {
		this.idCliente = idCliente;
		this.idOferta = idOferta;
		this.dataCriacao = dataCriacao;
		this.dataExpiracao = dataExpiracao;
		this.origemOferta = origemOferta;
		this.status = status;
		this.ofertaAtiva = ofertaAtiva;
	}

	public String getIdCliente() {
		return this.idCliente;
	}

	public Integer getIdOferta() {
		return this.idOferta;
	}

	public LocalDateTime getDataCriacao() {
		return this.dataCriacao;
	}

	public LocalDateTime getDataExpiracao() {
		return this.dataExpiracao;
	}

	public Integer getOrigemOferta() {
		return this.origemOferta;
	}

	public String getStatus() {
		return this.status;
	}

	public boolean isOfertaAtiva() {
		return this.ofertaAtiva;
	}

	@Override
	public String toString() {
		return ObjectMapperUtils.asString(this);
	}



}