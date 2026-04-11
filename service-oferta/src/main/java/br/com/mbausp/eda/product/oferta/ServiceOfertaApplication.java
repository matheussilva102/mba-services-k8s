package br.com.mbausp.eda.product.oferta;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.Bean;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.mbausp.eda.product.oferta.utils.ObjectMapperUtils;

@ConfigurationPropertiesScan
@SpringBootApplication
public class ServiceOfertaApplication {

	public static void main(String[] args) {
		SpringApplication.run(ServiceOfertaApplication.class, args);
	}

	@Bean
	public ObjectMapper objectMapper() {
		System.out.println("teste");
		return ObjectMapperUtils.defaultInstance();
	}

}
