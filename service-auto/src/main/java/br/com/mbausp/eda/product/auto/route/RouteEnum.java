package br.com.mbausp.eda.product.auto.route;

public enum RouteEnum {

	DIRECT_CONSULTAR_AUTO("{{camel.routes.direct.consultar-auto}}", "direct_consultar_auto_route_id"),
	DIRECT_CRIAR_AUTO("{{camel.routes.direct.criar-auto}}", "direct_criar_auto_route_id"),
	KAFKA_TOPICO_NOTIFICAR_AUTO("{{camel.routes.kafka.notificar-auto}}", "kafka_notificar_auto_route_id");

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
