package com.tistory.ospace.simpleproject.configuration.customMapper;

import com.fasterxml.jackson.databind.ObjectMapper;

public class CustomObjectMapper extends ObjectMapper {

	private static final long serialVersionUID = 1L;

	public CustomObjectMapper() {
		getSerializerProvider().setNullValueSerializer(new NullSerializer());
	}

}
