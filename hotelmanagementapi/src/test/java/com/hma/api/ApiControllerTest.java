package com.hma.api;
/*
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureRestTestClient;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.client.RestTestClient;

import com.hma.api.authentication.JwtService;
import com.hma.api.users.UserRepo;

@WebMvcTest(ApiController.class)
@AutoConfigureRestTestClient
@Import(SecurityConfiguration.class)
class ApiControllerTest {
	@Autowired
	private RestTestClient client;
	@MockitoBean
	private AuthenticationManager authManager;
	@MockitoBean
	private JwtService jwtService;

	@MockitoBean
	private AuthenticationEntryPoint authenticationEntryPoint;

	@MockitoBean
	private UserRepo repo;

	@Test
	void login_WithValidCredentials_ExpectedReturnTokens() {
		String username = "test";
		String password = "pass";
		LoginData loginData = new LoginData(username, password);
		JwtService.JwtTokenContainer accessMock = new JwtService.JwtTokenContainer("accessCodeForAccess", 3000);
		JwtService.JwtTokenContainer refreshMock = new JwtService.JwtTokenContainer("accessCodeForRefresh", 3000);
		when(authManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(null);
		when(jwtService.generateAccessToken(username, any())).thenReturn(accessMock);
		when(jwtService.generateRefreshToken(username, any())).thenReturn(refreshMock);

		client.post().uri("/api/login").contentType(MediaType.APPLICATION_JSON).body(loginData).exchange()
				.expectStatus().isOk().expectBody().jsonPath("$.access_token").isEqualTo(accessMock.token())
				.jsonPath("$.refresh_token").isEqualTo(refreshMock.token()).jsonPath("$.token_type").isEqualTo("Bearer")
				.jsonPath("$.expires_in").isEqualTo(accessMock.expiration());

	}

	
	 * @Test void refresh_WithValidRefreshCode_ExpectedReturnTokens() {
	 * 
	 * RefreshTokenCarrier carrier = new RefreshTokenCarrier("TestRefreshToken");
	 * JwtService.JwtTokenContainer accessMock = new
	 * JwtService.JwtTokenContainer("accessCodeForAccess", 3000);
	 * when(jwtService.generateAccessTokenFromRefreshToken(anyString())).thenReturn(
	 * accessMock);
	 * 
	 * client.post().uri("/api/refresh").contentType(MediaType.APPLICATION_JSON).
	 * body(carrier).exchange()
	 * .expectStatus().isOk().expectBody().jsonPath("$.access_token").isEqualTo(
	 * accessMock.token())
	 * .jsonPath("$.refresh_token").isEqualTo(carrier.refreshToken()).jsonPath(
	 * "$.token_type") .isEqualTo("Bearer")
	 * .jsonPath("$.expires_in").isEqualTo(accessMock.expiration());
	 * 
	 * }
	 * 
	 * @Test void login_WithInvalidCredentials_ExpectedStatusUnauthorised() { String
	 * username = "test"; String password = "pass"; LoginData loginData = new
	 * LoginData(username, password);
	 * when(authManager.authenticate(any(UsernamePasswordAuthenticationToken.class))
	 * ) .thenThrow(new BadCredentialsException("Bad Credentials"));
	  client.post().uri("/api/login").contentType(MediaType.APPLICATION_JSON).body(
	  loginData).exchange() .expectStatus().isUnauthorized(); }
	 
}
*/