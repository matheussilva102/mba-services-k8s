package br.com.mbausp.eda.product.conta.route;

public enum RouteEnum {

	DIRECT_CONSULTAR_CONTA("{{camel.routes.direct.consultar-conta}}", "direct_consultar_conta_route_id"),
	DIRECT_CRIAR_CONTA("{{camel.routes.direct.criar-conta}}", "direct_criar_conta_route_id"),
	KAFKA_TOPICO_NOTIFICAR_CONTA("{{camel.routes.kafka.notificar-conta}}", "kafka_notificar_conta_route_id");

	private String route;
	private String routeId;

	private RouteEnum(String route, String routeId) {
		this.route = route;
		this.routeId = routeId;
	}

	public String getRoute() {
		return route;
	}

	public String getRouteId() {
		return routeId;
	}


}
