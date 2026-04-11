package br.com.mbausp.eda.product.oferta.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;

@ConfigurationProperties(prefix = "runtime.props")
@RefreshScope
public class PropertiesConfig {

	private boolean unavailable;

	private int minLatencyInMilli;

	private int maxLatencyInMilli;

	private boolean producerDlq;

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

	public boolean isProducerDlq() {
		return producerDlq;
	}

	public void setProducerDlq(boolean producerDlq) {
		this.producerDlq = producerDlq;
	}

}
