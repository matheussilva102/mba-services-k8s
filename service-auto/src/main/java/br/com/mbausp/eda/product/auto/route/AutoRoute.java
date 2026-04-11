package br.com.mbausp.eda.product.auto.route;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

import org.apache.camel.Exchange;
import org.apache.camel.ValidationException;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.jackson.JacksonDataFormat;
import org.springframework.stereotype.Component;

import br.com.mbausp.eda.product.auto.config.PropertiesConfig;
import br.com.mbausp.eda.product.auto.domain.AutoCliente;
import br.com.mbausp.eda.product.auto.domain.AutoClienteItem;
import br.com.mbausp.eda.product.auto.domain.AutoClientePayload;
import br.com.mbausp.eda.product.auto.domain.AutoStatus;
import br.com.mbausp.eda.product.auto.exception.NotFoundException;
import br.com.mbausp.eda.product.auto.exception.UnavailableException;
import br.com.mbausp.eda.product.auto.repository.AutoClienteRepository;
import br.com.mbausp.eda.product.auto.utils.ObjectMapperUtils;

@Component
public class AutoRoute extends RouteBuilder {

	private static final int HTTP_ERROR_CODE_400 = 400;

	private static final int HTTP_ERROR_CODE_404 = 404;

	private static final int HTTP_ERROR_CODE_503 = 503;

	private final AutoClienteRepository autoClienteRepository;

	private final PropertiesConfig props;

    public AutoRoute(AutoClienteRepository autoUsuarioRepository, PropertiesConfig props) {
    	this.autoClienteRepository = autoUsuarioRepository;
    	this.props = props;
	}

	@Override
    public void configure() throws Exception {

		onException(NotFoundException.class)
    	.id("on_exception_id")
    		.handled(true)
    		.setBody(ex -> {
    			return Map.of("message", "auto não encontrado");
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
	        .handled(true)
        	.setBody(ex -> {
    			return Map.of("message", "verifique os campos obrigatórios");
	        })
        	.setHeader(Exchange.HTTP_RESPONSE_CODE, constant(HTTP_ERROR_CODE_400))
        .end();

        from(RouteEnum.DIRECT_CONSULTAR_AUTO.getRoute())
        .routeId(RouteEnum.DIRECT_CONSULTAR_AUTO.getRouteId())
	        .choice()
	        	.when(exchange -> AutoRoute.this.props.isUnavailable())
	        	.throwException(new UnavailableException())
	        .end()
	        .setBody(ex -> {
	        	var id = ex.getIn().getHeader("idCliente", String.class);
	        	var entities = this.autoClienteRepository.findByClienteId(id).orElseThrow(() -> new NotFoundException());
	        	var autos = entities
	        		.parallelStream()
	        		.collect(Collectors.mapping(entity -> {
	        			return new AutoClienteItem(entity.getId(),
	        			entity.getNuContrato(),
	        			entity.getClienteId(),
	        			entity.getDataCriacao(),
	        			entity.getStatus());
	        		}, Collectors.toList()));

	        	return new AutoCliente(autos);
	        })
	        .process(ex -> {
	        	// simular latencia
	        	var min = AutoRoute.this.props.getMinLatencyInMilli();
	        	var max = AutoRoute.this.props.getMaxLatencyInMilli();
	        	var randomNum = ThreadLocalRandom.current().nextInt(min, max);
	        	Thread.sleep(randomNum);
	        })
        .end();

        from(RouteEnum.DIRECT_CRIAR_AUTO.getRoute())
        .routeId(RouteEnum.DIRECT_CRIAR_AUTO.getRouteId())
        	.to("bean-validator://autoValidator")
	        .setBody(ex -> {
	        	var input = ex.getIn().getBody(AutoClientePayload.class);
	        	var rEntity = this.autoClienteRepository.saveConditionally(
	        			input.idCliente(),
	        			input.contractNumber(),
	        			AutoStatus.ATIVO.name(),
	        			LocalDateTime.now());

	        	return new AutoClienteItem(rEntity.getId(),
	        			rEntity.getNuContrato(),
	        			rEntity.getClienteId(),
	        			rEntity.getDataCriacao(),
	        			rEntity.getStatus());
	        })
	        .choice()
	        	.when(ex -> AutoRoute.this.props.isNotifyKafka())
			        .setProperty("result", body())
			        .marshal(new JacksonDataFormat(ObjectMapperUtils.defaultInstance(), AutoClienteItem.class))
			        .convertBodyTo(String.class)
			        .to(RouteEnum.KAFKA_TOPICO_NOTIFICAR_AUTO.getRoute())
			        	.id("to_kafka_notificar_auto_id")
			        .setBody(exchangeProperty("result"))
	        .end()
        .end();

    }

}
