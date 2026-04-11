package br.com.mbausp.eda.product.conta.entity;

import java.time.LocalDateTime;

import br.com.mbausp.eda.product.conta.domain.ContaStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "conta_cliente", schema = "conta")
public class ContaClienteEntity {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

	@Column(name = "cliente_id", nullable = false)
    private String clienteId;

	@Column(name = "nu_conta", nullable = false)
    private Long nuConta;

	@Column(name = "data_criacao", nullable = false)
    private LocalDateTime dataCriacao;

	@Enumerated(EnumType.STRING)
	@Column(name = "status", nullable = false)
    private ContaStatus status;

	public int getId() {
		return this.id;
	}

	public String getClienteId() {
		return this.clienteId;
	}

	public void setClienteId(String clienteId) {
		this.clienteId = clienteId;
	}

	public Long getNuConta() {
		return this.nuConta;
	}

	public void setNuConta(Long nuConta) {
		this.nuConta = nuConta;
	}

	public LocalDateTime getDataCriacao() {
		return this.dataCriacao;
	}

	public void setDataCriacao(LocalDateTime dataCriacao) {
		this.dataCriacao = dataCriacao;
	}

	public ContaStatus getStatus() {
		return this.status;
	}

	public void setStatus(ContaStatus status) {
		this.status = status;
	}


}
