package org.springframework.http;

import com.pagamento.common.dto.CartaoDTO;

import jakarta.validation.Valid;

public class ResponseEntity {

	public static Object ok1(@Valid CartaoDTO cartaoDTO) {
		// TODO Auto-generated method stub
		return null;
	}

	public Object getStatusCodeValue() {
		// TODO Auto-generated method stub
		return null;
	}

	public static ResponseEntity ok11(@Valid CartaoDTO cartaoDTO) {
		// TODO Auto-generated method stub
		return null;
	}

	public static ResponseEntity ok(@Valid CartaoDTO cartaoDTO) {
		// TODO Auto-generated method stub
		return null;
	}

	public HttpStatus getStatusCode() {
		// TODO Auto-generated method stub
		return null;
	}

}
