package br.com.mbausp.eda.product.oferta.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "oferta_cliente", schema = "oferta")
public class OfertaClienteEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

	@Column(name = "cliente_id", nullable = false)
    private String clienteId;

	@Column(name = "oferta_id", nullable = false)
    private int ofertaId;

	@Column(name = "oferta_ativa", nullable = false)
    private Boolean ofertaAtiva;

	@Column(name = "data_criacao", nullable = false)
    private LocalDateTime dataCriacao;

	@Column(name = "data_expiracao", nullable = false)
    private LocalDateTime dataExpiracao;

	@Column(name = "status", nullable = false)
    private String status;

	@Column(name = "origem_oferta", nullable = false)
    private int origemOferta;

	public int getId() {
		return this.id;
	}

	public String getClienteId() {
		return this.clienteId;
	}

	public void setClienteId(String clienteId) {
		this.clienteId = clienteId;
	}

	public int getOfertaId() {
		return this.ofertaId;
	}

	public void setOfertaId(int ofertaId) {
		this.ofertaId = ofertaId;
	}

	public Boolean getOfertaAtiva() {
		return this.ofertaAtiva;
	}

	public void setOfertaAtiva(Boolean ofertaAtiva) {
		this.ofertaAtiva = ofertaAtiva;
	}

	public LocalDateTime getDataCriacao() {
		return this.dataCriacao;
	}

	public void setDataCriacao(LocalDateTime dataCriacao) {
		this.dataCriacao = dataCriacao;
	}

	public LocalDateTime getDataExpiracao() {
		return this.dataExpiracao;
	}

	public void setDataExpiracao(LocalDateTime dataExpiracao) {
		this.dataExpiracao = dataExpiracao;
	}

	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public int getOrigemOferta() {
		return this.origemOferta;
	}

	public void setOrigemOferta(int origemOferta) {
		this.origemOferta = origemOferta;
	}


}
