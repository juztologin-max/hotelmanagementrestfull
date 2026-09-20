package com.hma.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.hma.api.authentication.JwtAuthenticationFilter;

import jakarta.servlet.DispatcherType;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class BaseConfiguration {

    @Bean
    SecurityFilterChain getSecurityFilterChain(HttpSecurity http, JwtAuthenticationFilter jwtAuthenticationFilter,
            AuthenticationEntryPoint authenticationEntryPoint, CorsConfigurationSource configurationSource) {
        //@formatter:off
		return http.authorizeHttpRequests(auth -> 
		                auth.requestMatchers("/api/auth/login","/api/auth/refresh","/api/auth/logout","/api/customers/new-customer","/api/auth/is-username-available").permitAll()
                            .requestMatchers("/api/admin/**").hasRole("ADMIN")
		                    .requestMatchers("/api/auth/login/test").authenticated()
		                    .dispatcherTypeMatchers(DispatcherType.ERROR).permitAll()     
		                    .anyRequest().denyAll())
                   .csrf(AbstractHttpConfigurer::disable)
                   .sessionManagement(session -> 
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                   .cors(corsCustomizeir->corsCustomizeir.configurationSource(configurationSource))
                   .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                   .exceptionHandling(ex->ex.authenticationEntryPoint(authenticationEntryPoint))
				   .build();
		//@formatter:on
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    AuthenticationManager authenticationManager(UserDetailsService userDetailsService,
            PasswordEncoder passwordEncoder) {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider(userDetailsService);
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder);
        ProviderManager prov = new ProviderManager(daoAuthenticationProvider);
        return prov;

    }

    @Bean
    CorsConfigurationSource configurationSource(@Value("${CORS_Origin}") String origin) {
        UrlBasedCorsConfigurationSource corsConfigurationSource = new UrlBasedCorsConfigurationSource();
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.setAllowedOrigins(List.of(origin));
        corsConfiguration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE"));
        corsConfiguration.setAllowedHeaders(List.of("*"));
        corsConfiguration.setAllowCredentials(true);
        corsConfigurationSource.registerCorsConfiguration("/**", corsConfiguration);
        return corsConfigurationSource;
    }
}
