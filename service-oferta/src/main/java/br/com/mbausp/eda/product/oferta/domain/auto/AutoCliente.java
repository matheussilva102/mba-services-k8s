package br.com.mbausp.eda.product.oferta.domain.auto;

import java.util.List;

import org.springframework.util.CollectionUtils;

import com.fasterxml.jackson.annotation.JsonIgnore;

public record AutoCliente(List<AutoClienteItem> autos) {

	@JsonIgnore
	public boolean hasAutos() {
		return !CollectionUtils.isEmpty(autos());
	}

	@JsonIgnore
	public AutoClienteItem getFirst() {
		return autos().getFirst();
	}

}