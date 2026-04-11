package br.com.mbausp.eda.product.oferta.domain.conta;

import java.util.List;

import org.springframework.util.CollectionUtils;

import com.fasterxml.jackson.annotation.JsonIgnore;

public record ContaCliente(List<ContaClienteItem> accounts) {

	@JsonIgnore
	public boolean hasAccounts() {
		return !CollectionUtils.isEmpty(accounts());
	}

	@JsonIgnore
	public ContaClienteItem getFirst() {
		return accounts().getFirst();
	}
}