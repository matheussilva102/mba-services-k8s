package br.com.mbausp.eda.product.conta.route;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.rest.RestBindingMode;
import org.springframework.stereotype.Component;

import br.com.mbausp.eda.product.conta.domain.ContaClientePayload;

@Component
public class HttpRest extends RouteBuilder {

    @Override
    public void configure() throws Exception {

        restConfiguration()
            .component("platform-http")
            .bindingMode(RestBindingMode.json)
            .contextPath("/accounts")
            .apiContextPath("/api-doc")
            .apiProperty("api.title", "Conta API")
            .apiProperty("api.version", "1.0.0");

        rest()
	        .description("contas de cliente")
	        .get("/{idCliente}")
	            .to(RouteEnum.DIRECT_CONSULTAR_CONTA.getRoute())
	        .post("/")
		        .type(ContaClientePayload.class)
		        .to(RouteEnum.DIRECT_CRIAR_CONTA.getRoute());

    }

}
