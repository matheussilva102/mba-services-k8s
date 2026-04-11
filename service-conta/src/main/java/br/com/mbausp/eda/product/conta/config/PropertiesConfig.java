package br.com.mbausp.eda.product.conta.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;

import com.fasterxml.jackson.core.JsonProcessingException;

import br.com.mbausp.eda.product.conta.utils.ObjectMapperUtils;

@ConfigurationProperties(prefix = "runtime.props")
@RefreshScope
public class PropertiesConfig {

	private boolean unavailable;

	private int minLatencyInMilli;

	private int maxLatencyInMilli;

	private boolean notifyKafka;

	public boolean isUnavailable() {
		return this.unavailable;
	}

	public void setUnavailable(boolean unavailable) {
		this.unavailable = unavailable;
	}

	public int getMinLatencyInMilli() {
		return this.minLatencyInMilli;
	}

	public void setMinLatencyInMilli(int minLatencyInMilli) {
		this.minLatencyInMilli = minLatencyInMilli;
	}

	public int getMaxLatencyInMilli() {
		return this.maxLatencyInMilli;
	}

	public void setMaxLatencyInMilli(int maxLatencyInMilli) {
		this.maxLatencyInMilli = maxLatencyInMilli;
	}

	public boolean isNotifyKafka() {
		return this.notifyKafka;
	}

	public void setNotifyKafka(boolean notifyKafka) {
		this.notifyKafka = notifyKafka;
	}

	@Override
	public String toString() {
		try {
			return ObjectMapperUtils.defaultInstance().writeValueAsString(this);
		} catch (JsonProcessingException e) {
			return "{\"notifyKafka\":%b}".formatted(this.notifyKafka);
		}
	}
	
	

}
