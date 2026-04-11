package br.com.mbausp.eda.product.conta;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.Bean;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.mbausp.eda.product.conta.utils.ObjectMapperUtils;

@ConfigurationPropertiesScan
@SpringBootApplication
public class ServiceContaApplication {

	public static void main(String[] args) {
		SpringApplication.run(ServiceContaApplication.class, args);
	}

	@Bean
	public ObjectMapper objectMapper() {
		return ObjectMapperUtils.defaultInstance();
	}

}
