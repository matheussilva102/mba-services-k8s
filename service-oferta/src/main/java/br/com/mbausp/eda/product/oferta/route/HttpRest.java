package br.com.mbausp.eda.product.oferta.route;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.rest.RestBindingMode;
import org.springframework.stereotype.Component;

@Component
public class HttpRest extends RouteBuilder {

    @Override
    public void configure() throws Exception {

        restConfiguration()
            .component("platform-http")
            .bindingMode(RestBindingMode.json)
            .contextPath("/offers")
            .apiContextPath("/api-doc")
            .apiProperty("api.title", "Oferta API")
            .apiProperty("api.version", "1.0.0");

        rest()
	        .description("ofertas de cliente")
	        .get("/{clientId}")
	            .to(RouteEnum.DIRECT_CONSULTAR_OFERTA.getRoute())
	        .get("/sincrona/{clientId}")
	        .to(RouteEnum.DIRECT_CONSULTAR_OFERTA_BLOQUEANTE.getRoute());

    }

}
