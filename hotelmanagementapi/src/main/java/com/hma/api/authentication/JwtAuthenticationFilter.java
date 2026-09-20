package com.hma.api.authentication;

import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.hma.api.users.LoginUser;
import com.hma.api.users.userroles.UserRole;

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
    AuthenticationEntryPoint authenticationEntryPoint;

    public JwtAuthenticationFilter(JwtService jwtService, AuthenticationEntryPoint authenticationEntryPoint) {
        this.jwtService = jwtService;
        this.authenticationEntryPoint = authenticationEntryPoint;
    }

    @Override
    public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        if (SecurityContextHolder.getContext().getAuthentication() == null) {
            String authHeader = request.getHeader("Authorization");
            if (authHeader != null && authHeader.startsWith("Bearer")) {
                String jws = authHeader.substring("Bearer ".length());
                if (jws == null || jws.isBlank()) {
                    authenticationEntryPoint.commence(request, response,
                            new BadCredentialsException("Missing ACCESS token in Authorization Header"));
                    return;
                }
                try {
                    Claims claims = jwtService.extractClaims(jws);
                    String type = claims.get("type", String.class);
                    if (type == null || !type.equals("ACCESS")) {
                        authenticationEntryPoint.commence(request, response,
                                new BadCredentialsException("Incorrect Token type"));
                        return;
                    }
                    List<?> claimsList = claims.get("roles", List.class);
                    Set<UserRole> roles = claimsList.stream()
                            .map((Object o) -> new UserRole(o.toString().toUpperCase()))
                            .collect(Collectors.toSet());
                    LoginUser user = LoginUser.builder(claims.getSubject(), null).addAllRoles(roles).build();

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
