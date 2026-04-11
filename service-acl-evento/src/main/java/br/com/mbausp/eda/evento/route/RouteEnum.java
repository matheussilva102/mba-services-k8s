package br.com.mbausp.eda.evento.route;

public enum RouteEnum {

	KAFKA_TOPICO_NOTIFICAR_CONTA("{{camel.routes.kafka.notificar-conta}}", "kafka_notificar_conta_route_id"),
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
