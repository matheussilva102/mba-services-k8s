package br.com.mbausp.eda.evento.route;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.jackson.JacksonDataFormat;
import org.springframework.stereotype.Component;

import br.com.mbausp.eda.evento.domain.Constants;
import br.com.mbausp.eda.evento.domain.ParsedMessage;
import br.com.mbausp.eda.evento.domain.auto.AutoClienteItem;
import br.com.mbausp.eda.evento.domain.conta.ContaClienteItem;
import br.com.mbausp.eda.evento.utils.ObjectMapperUtils;

@Component
public class AclEventoRoute extends RouteBuilder {

	@Override
    public void configure() throws Exception {

        from(RouteEnum.KAFKA_TOPICO_NOTIFICAR_CONTA.getRoute())
        .routeId(RouteEnum.KAFKA_TOPICO_NOTIFICAR_CONTA.getRouteId())
        	.unmarshal(new JacksonDataFormat(ObjectMapperUtils.defaultInstance(), ContaClienteItem.class))
	        .setBody(ex -> {
	        	var input = ex.getIn().getBody(ContaClienteItem.class);
	        	var idCliente = input.idCliente();
	        	var dataCriacao = input.dataCriacao();
	        	return new ParsedMessage(
	        			idCliente,
	        			Constants.ORIGEM_OFERTA_CONTA_ID,
	        			dataCriacao,
	        			Constants.ORIGEM_OFERTA_CONTA_ID);
	        })
	        .marshal(new JacksonDataFormat(ObjectMapperUtils.defaultInstance(), ParsedMessage.class))
	        .toD("{{camel.routes.kafka.notificar-oferta}}")
	        	.id("tod_kafka_notificar_oferta_conta_id")
	        .log("mensagem de conta produzida para kafka >> ${body}")
        .end();

        from(RouteEnum.KAFKA_TOPICO_NOTIFICAR_AUTO.getRoute())
        .routeId(RouteEnum.KAFKA_TOPICO_NOTIFICAR_AUTO.getRouteId())
	        .unmarshal(new JacksonDataFormat(ObjectMapperUtils.defaultInstance(), AutoClienteItem.class))
	        .setBody(ex -> {
	        	var input = ex.getIn().getBody(AutoClienteItem.class);
	        	var idCliente = input.idCliente();
	        	var dataCriacao = input.dataCriacao();
	        	return new ParsedMessage(
	        			idCliente,
	        			Constants.ORIGEM_OFERTA_AUTO_ID,
	        			dataCriacao,
	        			Constants.ORIGEM_OFERTA_AUTO_ID);
	        })
	        .marshal(new JacksonDataFormat(ObjectMapperUtils.defaultInstance(), ParsedMessage.class))
	        .toD("{{camel.routes.kafka.notificar-oferta}}")
	        	.id("tod_kafka_notificar_oferta_auto_id")
	        .log("mensagem de auto produzida para kafka >> ${body}")
        .end();

    }

}
