package br.com.mbausp.eda.product.oferta.route;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.jackson.JacksonDataFormat;
import org.springframework.stereotype.Component;

import br.com.mbausp.eda.product.oferta.config.PropertiesConfig;
import br.com.mbausp.eda.product.oferta.domain.Constants;
import br.com.mbausp.eda.product.oferta.domain.ParsedMessage;
import br.com.mbausp.eda.product.oferta.domain.auto.AutoCliente;
import br.com.mbausp.eda.product.oferta.domain.conta.ContaCliente;
import br.com.mbausp.eda.product.oferta.domain.oferta.OfertaCliente;
import br.com.mbausp.eda.product.oferta.domain.oferta.OfertaClienteItem;
import br.com.mbausp.eda.product.oferta.domain.oferta.OfertaStatus;
import br.com.mbausp.eda.product.oferta.entity.OfertaClienteEntity;
import br.com.mbausp.eda.product.oferta.repository.OfertaClienteRepository;
import br.com.mbausp.eda.product.oferta.utils.ObjectMapperUtils;

@Component
public class OfertaRouteBloqueante extends RouteBuilder {

	private final Function<ParsedMessage, OfertaClienteEntity> saveConditionallyFunction = input -> {
		return this.ofertaClienteRepository.saveConditionally(
    			input.getIdCliente(),
    			input.getIdOferta(),
    			input.isOfertaAtiva(),
    			input.getDataCriacao(),
    			input.getDataExpiracao(),
    			input.getStatus(),
    			input.getOrigemOferta()
    			);
	};

	private OfertaClienteRepository ofertaClienteRepository;

	private final PropertiesConfig props;

    public OfertaRouteBloqueante(OfertaClienteRepository ofertaUsuarioRepository, PropertiesConfig props) {
    	this.ofertaClienteRepository = ofertaUsuarioRepository;
    	this.props = props;
	}

	@Override
    public void configure() throws Exception {

		from(RouteEnum.DIRECT_CONSULTAR_OFERTA_BLOQUEANTE.getRoute())
        .routeId(RouteEnum.DIRECT_CONSULTAR_OFERTA_BLOQUEANTE.getRouteId())
        	.setProperty("clientId", header("clientId"))
        	.removeHeaders("*")
        	.setHeader(Exchange.HTTP_METHOD, constant("GET"))
        	.toD("{{camel.routes.http.consultar-conta}}${exchangeProperty.clientId}")
        		.id("tod_http_consultar_conta_id")
        	.convertBodyTo(byte[].class)
        	.unmarshal(new JacksonDataFormat(ObjectMapperUtils.defaultInstance(), ContaCliente.class))
        	.log("response conta >> ${body}")
        	.setProperty("conta-result", body())
        	.removeHeaders("*")
        	.setHeader(Exchange.HTTP_METHOD, constant("GET"))
        	.toD("{{camel.routes.http.consultar-auto}}${exchangeProperty.clientId}")
    			.id("tod_http_consultar_auto_id")
			.convertBodyTo(byte[].class)
			.unmarshal(new JacksonDataFormat(ObjectMapperUtils.defaultInstance(), AutoCliente.class))
			.log("response auto >> ${body}")
			.setProperty("auto-result", body())
        	.process(ex -> {
        		var paramsSaveConditionally = new ArrayList<ParsedMessage>();
        		var accounts = ex.getProperty("conta-result", ContaCliente.class);
        		if (accounts.hasAccounts()) {
        			var input = accounts.getFirst();
        			var dataExpiracao = LocalDate.now().plusMonths(3).atTime(23,59,59);
    	        	var item = new ParsedMessage(
    	        			input.idCliente(),
    	        			Constants.ORIGEM_OFERTA_CONTA_ID,
    	        			input.dataCriacao(),
    	        			dataExpiracao,
    	        			Constants.ORIGEM_OFERTA_CONTA_ID,
    	        			OfertaStatus.ATIVA.name(),
    	        			true);
    	        	paramsSaveConditionally.add(item);
        		}

        		var autos = ex.getProperty("auto-result", AutoCliente.class);
        		if (autos.hasAutos()) {
        			var input = autos.getFirst();
        			var dataExpiracao = LocalDate.now().plusMonths(3).atTime(23,59,59);
    	        	var item = new ParsedMessage(
    	        			input.idCliente(),
    	        			Constants.ORIGEM_OFERTA_AUTO_ID,
    	        			input.dataCriacao(),
    	        			dataExpiracao,
    	        			Constants.ORIGEM_OFERTA_AUTO_ID,
    	        			OfertaStatus.ATIVA.name(),
    	        			true);
    	        	paramsSaveConditionally.add(item);
        		}

        		var items = paramsSaveConditionally.stream().collect(Collectors.mapping(saveConditionally -> {
        			var rEntity = this.saveConditionallyFunction.apply(saveConditionally);
        			return new OfertaClienteItem(rEntity.getId(),
    	        			rEntity.getClienteId(),
    	        			rEntity.getOfertaId(),
    	        			rEntity.getOfertaAtiva(),
    	        			rEntity.getDataCriacao(),
        					rEntity.getDataExpiracao(),
        					rEntity.getStatus(),
        					rEntity.getOrigemOferta());
        		}, Collectors.toList()));

        		ex.setProperty("route-result", new OfertaCliente(items));
        	})
        	.setBody(exchangeProperty("route-result"))
        	.process(ex -> {
	        	// simular latencia
	        	var min = OfertaRouteBloqueante.this.props.getMinLatencyInMilli();
	        	var max = OfertaRouteBloqueante.this.props.getMaxLatencyInMilli();
	        	var randomNum = ThreadLocalRandom.current().nextInt(min, max);
	        	Thread.sleep(randomNum);
	        })
        .end();
    }

}
