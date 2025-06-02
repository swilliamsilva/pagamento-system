package com.pagamento.service;

import java.util.List;
import java.util.function.BooleanSupplier;

import org.springframework.security.core.Authentication;

public class JwtTokenProvider {

	public JwtTokenProvider(String secret, long validity) {
		// TODO Auto-generated constructor stub
	}

	public Object createToken(String anyString, List<Object> anyList) {
		// TODO Auto-generated method stub
		return null;
	}

	public String createToken(Authentication authentication) {
		// TODO Auto-generated method stub
		return null;
	}

	public BooleanSupplier validateToken(String token) {
		// TODO Auto-generated method stub
		return null;
	}

}
