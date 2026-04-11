package br.com.mbausp.eda.product.conta.route;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

import org.apache.camel.Exchange;
import org.apache.camel.ValidationException;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.jackson.JacksonDataFormat;
import org.springframework.stereotype.Component;

import br.com.mbausp.eda.product.conta.config.PropertiesConfig;
import br.com.mbausp.eda.product.conta.domain.ContaCliente;
import br.com.mbausp.eda.product.conta.domain.ContaClienteItem;
import br.com.mbausp.eda.product.conta.domain.ContaClientePayload;
import br.com.mbausp.eda.product.conta.domain.ContaStatus;
import br.com.mbausp.eda.product.conta.exception.NotFoundException;
import br.com.mbausp.eda.product.conta.exception.UnavailableException;
import br.com.mbausp.eda.product.conta.repository.ContaClienteRepository;
import br.com.mbausp.eda.product.conta.utils.ObjectMapperUtils;

@Component
public class ContaRoute extends RouteBuilder {

	private static final int HTTP_ERROR_CODE_400 = 400;

	private static final int HTTP_ERROR_CODE_404 = 404;

	private static final int HTTP_ERROR_CODE_503 = 503;

	private final ContaClienteRepository contaClienteRepository;

	private final PropertiesConfig props;

    public ContaRoute(ContaClienteRepository contaUsuarioRepository, PropertiesConfig props) {
    	this.contaClienteRepository = contaUsuarioRepository;
    	this.props = props;
	}

	@Override
    public void configure() throws Exception {

		onException(NotFoundException.class)
    	.id("on_exception_id")
    		.handled(true)
    		.setBody(ex -> {
    			return Map.of("message", "conta não encontrada");
    		})
    		.setHeader(Exchange.HTTP_RESPONSE_CODE, constant(HTTP_ERROR_CODE_404))
    	.end();

		onException(UnavailableException.class)
    	.id("on_exception_un_id")
    		.handled(true)
    		.setBody(ex -> {
    			return Map.of("message", "serviço indisponivel");
    		})
    		.setHeader(Exchange.HTTP_RESPONSE_CODE, constant(HTTP_ERROR_CODE_503))
    	.end();

    	onException(ValidationException.class)
    		.id("on_exception_validation_id")
	        .handled(true)
        	.setBody(ex -> {
    			return Map.of("message", "verifique os campos obrigatórios");
	        })
        	.setHeader(Exchange.HTTP_RESPONSE_CODE, constant(HTTP_ERROR_CODE_400))
        .end();

        from(RouteEnum.DIRECT_CONSULTAR_CONTA.getRoute())
        .routeId(RouteEnum.DIRECT_CONSULTAR_CONTA.getRouteId())
	        .choice()
	        	.when(exchange -> ContaRoute.this.props.isUnavailable())
	        	.throwException(new UnavailableException())
	        .end()
	        .setBody(ex -> {
	        	var id = ex.getIn().getHeader("idCliente", String.class);
	        	var entities = this.contaClienteRepository.findByClienteId(id).orElseThrow(() -> new NotFoundException());
	        	var accounts = entities
	        		.parallelStream()
	        		.collect(Collectors.mapping(entity -> {
	        			return new ContaClienteItem(
	        					entity.getId(),
	    	        			entity.getNuConta(),
	    	        			entity.getClienteId(),
	    	        			entity.getDataCriacao(),
	    	        			entity.getStatus());
	        		}, Collectors.toList()));
	        	return new ContaCliente(accounts);
	        })
	        .process(ex -> {
	        	// simular latencia
	        	var min = ContaRoute.this.props.getMinLatencyInMilli();
	        	var max = ContaRoute.this.props.getMaxLatencyInMilli();
	        	var randomNum = ThreadLocalRandom.current().nextInt(min, max);
	        	Thread.sleep(randomNum);
	        })
        .end();

        from(RouteEnum.DIRECT_CRIAR_CONTA.getRoute())
        .routeId(RouteEnum.DIRECT_CRIAR_CONTA.getRouteId())
        	.to("bean-validator://contaValidator")
	        .setBody(ex -> {
	        	var input = ex.getIn().getBody(ContaClientePayload.class);
	        	var rEntity = this.contaClienteRepository.saveConditionally(
	        			input.idCliente(),
	        			input.number(),
	        			ContaStatus.ATIVA.name(),
	        			LocalDateTime.now());

	        	return new ContaClienteItem(
	        			rEntity.getId(),
	        			rEntity.getNuConta(),
	        			rEntity.getClienteId(),
	        			rEntity.getDataCriacao(),
	        			rEntity.getStatus());
	        })
	        .choice()
		        .when(ex -> ContaRoute.this.props.isNotifyKafka())
			        .setProperty("result", body())
			        .marshal(new JacksonDataFormat(ObjectMapperUtils.defaultInstance(), ContaClienteItem.class))
			        .convertBodyTo(String.class)
			        .to(RouteEnum.KAFKA_TOPICO_NOTIFICAR_CONTA.getRoute())
			        	.id("to_kafka_notificar_conta_id")
			        .setBody(exchangeProperty("result"))
	        .end()
        .end();

    }

}
