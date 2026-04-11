package br.com.mbausp.eda.product.oferta.route;

import java.time.LocalDate;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.jackson.JacksonDataFormat;
import org.apache.camel.component.kafka.KafkaConstants;
import org.apache.camel.component.kafka.consumer.KafkaManualCommit;
import org.springframework.stereotype.Component;

import br.com.mbausp.eda.product.oferta.config.PropertiesConfig;
import br.com.mbausp.eda.product.oferta.domain.ParsedMessage;
import br.com.mbausp.eda.product.oferta.domain.ParsedMessageIn;
import br.com.mbausp.eda.product.oferta.domain.oferta.OfertaCliente;
import br.com.mbausp.eda.product.oferta.domain.oferta.OfertaClienteItem;
import br.com.mbausp.eda.product.oferta.domain.oferta.OfertaStatus;
import br.com.mbausp.eda.product.oferta.entity.OfertaClienteEntity;
import br.com.mbausp.eda.product.oferta.repository.OfertaClienteRepository;
//import br.com.mbausp.eda.product.oferta.entity.OfertaClienteEntity;
//import br.com.mbausp.eda.product.oferta.repository.OfertaClienteRepository;
import br.com.mbausp.eda.product.oferta.utils.ObjectMapperUtils;

@Component
public class OfertaRoute extends RouteBuilder {

	private static final Processor commitKafkaProcessor = ex -> {
        var manual = ex.getIn().getHeader(KafkaConstants.MANUAL_COMMIT, KafkaManualCommit.class);
        if (manual != null)
            manual.commit();
    };

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

    public OfertaRoute(
    		OfertaClienteRepository ofertaUsuarioRepository, 
    		PropertiesConfig props) {
    	this.ofertaClienteRepository = ofertaUsuarioRepository;
    	this.props = props;
	}

	@Override
    public void configure() throws Exception {

        from(RouteEnum.DIRECT_CONSULTAR_OFERTA.getRoute())
        .routeId(RouteEnum.DIRECT_CONSULTAR_OFERTA.getRouteId())
	        .setProperty("clientId", header("clientId"))
	    	.removeHeaders("*")
	        .setBody(ex -> {
	        	var id = ex.getProperty("clientId", String.class);
	        	var entities = this.ofertaClienteRepository.findByClienteId(id).orElse(List.of());
	        	var offers = entities
	        		.parallelStream()
	        		.collect(Collectors.mapping(item -> {
	        			return new OfertaClienteItem(item.getId(),
	        					item.getClienteId(),
	        					item.getOfertaId(),
	        					item.getOfertaAtiva(),
	        					item.getDataCriacao(),
	        					item.getDataExpiracao(),
	        					item.getStatus(),
	        					item.getOrigemOferta());
	        		}, Collectors.toList()));

	        	return new OfertaCliente(offers);
	        })
        .end();

        from(RouteEnum.KAFKA_TOPICO_NOTIFICAR_OFERTA.getRoute())
        .routeId(RouteEnum.KAFKA_TOPICO_NOTIFICAR_OFERTA.getRouteId())
	        .errorHandler(deadLetterChannel(RouteEnum.KAFKA_TOPICO_NOTIFICAR_OFERTA_DLQ.getRoute()))
				.id("errorhandler_kafka_topico_notificar_oferta_id")
			.onCompletion() //commit either with success or failed
				.process(commitKafkaProcessor)
			.end()
        	.unmarshal(new JacksonDataFormat(ObjectMapperUtils.defaultInstance(), ParsedMessageIn.class))
	        .setBody(ex -> {
	        	var input = ex.getIn().getBody(ParsedMessageIn.class);
	        	var dataExpiracao = LocalDate.now().plusMonths(3).atTime(23,59,59);
	        	return new ParsedMessage(
	        			input.idCliente(),
	        			input.idOferta(),
	        			input.dataCriacao(),
	        			dataExpiracao,
	        			input.origemOferta(),
	        			OfertaStatus.ATIVA.name(),
	        			true);
	        })
	    	.choice()
	    		.when(ex -> OfertaRoute.this.props.isProducerDlq())
	        		.setBody(ex -> {
		    			var body = ex.getIn().getBody(ParsedMessage.class);
		    			return body.toString();
		    		})
	        		.throwException(RuntimeException.class, "simulando falha para publicar na DLQ")
	    	.end()
	    	.process(ex -> {
	        	var input = ex.getIn().getBody(ParsedMessage.class);
	        	this.saveConditionallyFunction.apply(input);
	        })
	    	.log("mensagem persistida no postgres >> ${body}")
        .end();

        from(RouteEnum.KAFKA_TOPICO_NOTIFICAR_OFERTA_DLQ.getRoute())
        .routeId(RouteEnum.KAFKA_TOPICO_NOTIFICAR_OFERTA_DLQ.getRouteId())
	        .onCompletion().onCompleteOnly() //commit only on success
				.process(commitKafkaProcessor)
			.end()
        	.unmarshal(new JacksonDataFormat(ObjectMapperUtils.defaultInstance(), ParsedMessage.class))
        	.process(ex -> {
	        	var input = ex.getIn().getBody(ParsedMessage.class);
	 	        this.saveConditionallyFunction.apply(input);
        	})
        	.log("mensagem persistida no postgres via DLQ >> ${body}")
        .end();
    }

}
