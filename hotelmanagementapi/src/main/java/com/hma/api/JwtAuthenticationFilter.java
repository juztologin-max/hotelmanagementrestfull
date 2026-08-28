package com.hma.api;

import java.io.IOException;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
	private final JwtService jwtService;
	private final UserDetailsService userDetailsService;
	AuthenticationEntryPoint authenticationEntryPoint;

	public JwtAuthenticationFilter(JwtService jwtService, UserDetailsService userDetailsService,
			AuthenticationEntryPoint authenticationEntryPoint) {
		this.jwtService = jwtService;
		this.userDetailsService = userDetailsService;
		this.authenticationEntryPoint = authenticationEntryPoint;
	}

	@Override
	public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		if (SecurityContextHolder.getContext().getAuthentication() == null) {
			String authHeader = request.getHeader("Authorization");
			if (authHeader != null && authHeader.startsWith("Bearer")) {
				String jws = authHeader.substring("Bearer ".length());
				try {
					Claims claims = jwtService.extractClaims(jws);
					String type = claims.get("type", String.class);
					if (type == null || !type.equals("ACCESS")) {
						authenticationEntryPoint.commence(request, response,
								new BadCredentialsException("Incorrect Token type"));
						return;
					}
					UserDetails user = userDetailsService.loadUserByUsername(claims.getSubject());
					UsernamePasswordAuthenticationToken authentication = UsernamePasswordAuthenticationToken
							.authenticated(user, null, user.getAuthorities());
					authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
					SecurityContextHolder.getContext().setAuthentication(authentication);
				} catch (ExpiredJwtException ex) {
					authenticationEntryPoint.commence(request, response, new BadCredentialsException("Expired token"));
					return;
				} catch (SignatureException ex) {
					authenticationEntryPoint.commence(request, response, new BadCredentialsException("Invalid token"));
					return;

				} catch (Exception ex) {
					authenticationEntryPoint.commence(request, response,
							new BadCredentialsException(ex.getClass().getSimpleName()));
					return;
				}

			}
		}
		filterChain.doFilter(request, response);

	}

}
