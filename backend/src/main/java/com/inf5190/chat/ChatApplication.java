package com.inf5190.chat;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;

import com.inf5190.chat.auth.filter.AuthFilter;

import com.inf5190.chat.auth.session.SessionDataAccessor;
import com.inf5190.chat.auth.session.SessionManager;

import java.util.Optional;

/**
 * Application spring boot.
 */
@SpringBootApplication
public class ChatApplication {

    public static void main(String[] args) {
        SpringApplication.run(ChatApplication.class, args);
    }

    @Value("${cors.allowedOrigins}")
	private String allowedOriginsProperty;

    /**
     * Fonction qui enregistre le filtre d'authorization.
     */
    @Bean
    public FilterRegistrationBean<AuthFilter> authenticationFilter(
            SessionDataAccessor sessionDataAccessor,
            SessionManager sessionManager) {
        FilterRegistrationBean<AuthFilter> registrationBean = new FilterRegistrationBean<>();

        registrationBean.setFilter(new AuthFilter(sessionDataAccessor,
                sessionManager));
        registrationBean.addUrlPatterns("/messages", "/auth/logout");

        return registrationBean;
    }

    @Bean("allowedOrigins")
	public String[] getAllowedOrigins() {
		return Optional.ofNullable(System.getenv("ALLOWED_ORIGINS"))
				.orElse(this.allowedOriginsProperty).split(",");
	}

}
