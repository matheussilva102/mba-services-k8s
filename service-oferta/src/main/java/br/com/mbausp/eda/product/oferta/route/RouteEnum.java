package br.com.mbausp.eda.product.oferta.route;

public enum RouteEnum {

	DIRECT_CONSULTAR_OFERTA_BLOQUEANTE("{{camel.routes.direct.consultar-oferta-bloqueante}}", "direct_consultar_oferta_bloqueante_route_id"),
	DIRECT_CONSULTAR_OFERTA("{{camel.routes.direct.consultar-oferta}}", "direct_consultar_oferta_route_id"),
	KAFKA_TOPICO_NOTIFICAR_OFERTA("{{camel.routes.kafka.notificar-oferta}}", "kafka_notificar_oferta_route_id"),
	KAFKA_TOPICO_NOTIFICAR_OFERTA_DLQ("{{camel.routes.kafka.notificar-oferta-dlq}}", "kafka_notificar_oferta_dlq_route_id");

	private String route;
	private String routeId;

	private RouteEnum(String route, String routeId) {
		this.route = route;
		this.routeId = routeId;
	}

	public String getRoute() {
		return this.route;
	}

	public String getRouteId() {
		return this.routeId;
	}


}
