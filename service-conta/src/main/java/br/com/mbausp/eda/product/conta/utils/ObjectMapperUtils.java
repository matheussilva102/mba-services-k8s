package br.com.mbausp.eda.product.conta.utils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;

public final class ObjectMapperUtils {

	private static final String YYYY_MM_DD = "yyyy-MM-dd";

    private static final String HH_MM_SS = "HH:mm:ss";

    private static final String YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";

    private static final Jackson2ObjectMapperBuilder jackson2Builder = Jackson2ObjectMapperBuilder.json()
    		.failOnUnknownProperties(false) //
            .serializerByType(LocalDate.class, new LocalDateSerializer(DateTimeFormatter.ofPattern(YYYY_MM_DD))) //
            .serializerByType(LocalTime.class, new LocalTimeSerializer(DateTimeFormatter.ofPattern(HH_MM_SS))) //
            .serializerByType(LocalDateTime.class, new LocalDateTimeSerializer(DateTimeFormatter.ofPattern(YYYY_MM_DD_HH_MM_SS))) //
            .deserializerByType(LocalDate.class, new LocalDateDeserializer(DateTimeFormatter.ofPattern(YYYY_MM_DD))) //
            .deserializerByType(LocalTime.class, new LocalTimeDeserializer(DateTimeFormatter.ofPattern(HH_MM_SS))) //
            .deserializerByType(LocalDateTime.class, new LocalDateTimeDeserializer(DateTimeFormatter.ofPattern(YYYY_MM_DD_HH_MM_SS))) //
            .featuresToDisable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS) //
            .serializationInclusion(JsonInclude.Include.NON_NULL) //
            .modulesToInstall(new JavaTimeModule());

    private static final ObjectMapper objectMapper = jackson2Builder.createXmlMapper(false).build();


	private ObjectMapperUtils() { }

	public static final ObjectMapper defaultInstance() {
		return objectMapper;
	}

	public static final ObjectMapper newInstance() {
		return jackson2Builder.createXmlMapper(false).build();
	}

}
