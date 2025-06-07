package com.pagamento.common.observability;

import org.springframework.web.servlet.config.annotation.InterceptorRegistry;

public interface WebMvcConfigurer {

	void addInterceptors(InterceptorRegistry registry);

}
