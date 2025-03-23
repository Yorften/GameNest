package com.gamenest.config.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import static org.springframework.security.config.Customizer.withDefaults;

import com.gamenest.config.jwt.JWTAuthEntryPoint;
import com.gamenest.config.jwt.JWTAuthenticationFilter;
import com.gamenest.service.implementation.CustomUserDetailsServiceImpl;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

	private final JWTAuthEntryPoint authEntryPoint;
	private final CustomUserDetailsServiceImpl customUserDetailsService;
	private final JWTAuthenticationFilter jwtAuthenticationFilter;

	@Bean
	public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
		AuthenticationManagerBuilder authenticationManagerBuilder = http
				.getSharedObject(AuthenticationManagerBuilder.class);
		authenticationManagerBuilder.userDetailsService(customUserDetailsService);
		return authenticationManagerBuilder.build();
	}

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
		httpSecurity
				.cors(cors -> cors.configurationSource(corsConfigurationSource()))
				.csrf(csrf -> csrf.disable())
				.formLogin(login -> login.disable())
				.authorizeHttpRequests(auth -> auth
						.requestMatchers("/api/v1/auth/login").permitAll()
						.requestMatchers("/api/v1/auth/register").permitAll()
						.requestMatchers("/api/v1/webhook").permitAll()
						.requestMatchers("/actuator/health").permitAll()
						.requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html", "v1/swagger-ui/**")
						.permitAll()
						.requestMatchers(HttpMethod.GET, "/api/v1/games").permitAll()
						.requestMatchers(HttpMethod.GET, "/api/v1/games/**").permitAll()
						.requestMatchers(HttpMethod.GET, "/api/v1/tags").permitAll()
						.requestMatchers(HttpMethod.GET, "/api/v1/categories").permitAll()
						.requestMatchers("/api/v1/users").hasAnyRole("ADMIN")
						.requestMatchers("/api/v1/categories").hasAnyRole("ADMIN")
						.requestMatchers("/api/v1/tags").hasAnyRole("ADMIN")
						.requestMatchers("/api/v1/games").hasAnyRole("ADMIN")
						.anyRequest().authenticated())
				.exceptionHandling(exception -> exception.authenticationEntryPoint(authEntryPoint))
				.sessionManagement(session -> session
						.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.httpBasic(withDefaults())
				.userDetailsService(customUserDetailsService);

		httpSecurity.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

		return httpSecurity.build();
	}

	@Bean
	public CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration configuration = new CorsConfiguration();
		configuration.addAllowedOrigin("http://localhost:5173");
		configuration.addAllowedMethod("*");
		configuration.addAllowedHeader("*");
		configuration.setAllowCredentials(true);

		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", configuration);
		return source;
	}

}
