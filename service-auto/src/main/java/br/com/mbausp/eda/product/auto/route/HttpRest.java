package br.com.mbausp.eda.product.auto.route;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.rest.RestBindingMode;
import org.springframework.stereotype.Component;

import br.com.mbausp.eda.product.auto.domain.AutoClientePayload;

@Component
public class HttpRest extends RouteBuilder {

    @Override
    public void configure() throws Exception {

        restConfiguration()
            .component("platform-http")
            .bindingMode(RestBindingMode.json)
            .contextPath("/autos")
            .apiContextPath("/api-doc")
            .apiProperty("api.title", "Auto API")
            .apiProperty("api.version", "1.0.0");

        rest()
	        .description("auto de cliente")
	        .get("/{idCliente}")
	            .to(RouteEnum.DIRECT_CONSULTAR_AUTO.getRoute())
	        .post("/")
		        .type(AutoClientePayload.class)
		        .to(RouteEnum.DIRECT_CRIAR_AUTO.getRoute());

    }

}
