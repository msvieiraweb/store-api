package com.spring.operacoes.config;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

// https://auth0.com/blog/implementing-jwt-authentication-on-spring-boot/

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private Environment env;
	
	//Endpoints liberados para acesso.
	public static final String[] PUBLIC_MATCHERS = { "/H2-console/**"};
	
	public static final String[] PUBLIC_MATCHERS_GET = { "/produtos/**", "/categorias/**", "/clientes/**" };

	//Padrão do framework - Todos os caminhos do PUBLIC_MATCHERS serão permitidos.
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		
		if (Arrays.asList(env.getActiveProfiles()).contains("test")) {
			http.headers().frameOptions().disable();
		}
		//Como não estamos usando sessão, não precisamos usar o CSRF, pois nosso sistema é STATELESS
		http.cors().and().csrf().disable();
		http.authorizeRequests().
		antMatchers(PUBLIC_MATCHERS).permitAll().
		antMatchers(HttpMethod.GET,PUBLIC_MATCHERS_GET).permitAll().
		anyRequest().authenticated();
		http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
	}

	//Permitindo acesso por multiplas fontes básico de todos os caminhos.
	@Bean
	CorsConfigurationSource corsConfigurationSource() {
		final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", new CorsConfiguration().applyPermitDefaultValues());
		return source;
	}
}