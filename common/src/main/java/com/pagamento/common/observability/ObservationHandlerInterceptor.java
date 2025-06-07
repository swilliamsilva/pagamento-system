package com.pagamento.common.observability;

import org.springframework.web.servlet.HandlerInterceptor;

import io.micrometer.observation.ObservationRegistry;

public class ObservationHandlerInterceptor implements HandlerInterceptor {

	public ObservationHandlerInterceptor(ObservationRegistry create) {
		// TODO Auto-generated constructor stub
	}

}
