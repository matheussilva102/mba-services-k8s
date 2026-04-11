package br.com.mbausp.eda.evento;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.Bean;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.mbausp.eda.evento.utils.ObjectMapperUtils;

@ConfigurationPropertiesScan
@SpringBootApplication
public class ServiceOfertaApplication {

	public static void main(String[] args) {
		SpringApplication.run(ServiceOfertaApplication.class, args);
	}

	@Bean
	public ObjectMapper objectMapper() {
		return ObjectMapperUtils.defaultInstance();
	}

}
