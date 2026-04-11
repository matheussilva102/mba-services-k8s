package br.com.mbausp.eda.product.auto.entity;

import java.time.LocalDateTime;

import br.com.mbausp.eda.product.auto.domain.AutoStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "auto_cliente", schema = "auto")
public class AutoClienteEntity {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

	@Column(name = "cliente_id", nullable = false)
    private String clienteId;

	@Column(name = "nu_contrato", nullable = false)
    private Integer nuContrato;

	@Enumerated(EnumType.STRING)
	@Column(name = "status", nullable = false)
    private AutoStatus status;

	@Column(name = "data_criacao", nullable = false)
    private LocalDateTime dataCriacao;

	public int getId() {
		return this.id;
	}

	public String getClienteId() {
		return this.clienteId.trim();
	}

	public void setClienteId(String clienteId) {
		this.clienteId = clienteId;
	}

	public Integer getNuContrato() {
		return this.nuContrato;
	}

	public void setNuContrato(Integer nuContrato) {
		this.nuContrato = nuContrato;
	}

	public AutoStatus getStatus() {
		return this.status;
	}

	public void setStatus(AutoStatus status) {
		this.status = status;
	}

	public LocalDateTime getDataCriacao() {
		return this.dataCriacao;
	}

	public void setDataCriacao(LocalDateTime dataCriacao) {
		this.dataCriacao = dataCriacao;
	}

}