package com.hma.api;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonProperty;

@RestController
@RequestMapping("/api")
public class ApiController {
	private final AuthenticationManager authManager;
	private final JwtService jwtService;

	public ApiController(AuthenticationManager authenticationManager, JwtService jwtService) {
		this.authManager = authenticationManager;
		this.jwtService = jwtService;
	}

	@PostMapping("/login")
	@ResponseStatus(HttpStatus.OK) // not needed as default is SUCCESS
	public TokenResponceBody postLoginHandler(@RequestBody LoginData loginData) {
		UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
				loginData.username(), loginData.password(), null);
		authManager.authenticate(authentication);
		JwtService.JwtTokenContainer refreshTokenContainer = jwtService.generateRefreshToken(loginData.username());
		JwtService.JwtTokenContainer accessTokenContainer = jwtService.generateAccessToken(loginData.username());

		return new TokenResponceBody(accessTokenContainer.token(), accessTokenContainer.expiration(),
				refreshTokenContainer.token());

	}

	@PostMapping("/refresh")
	@ResponseStatus(HttpStatus.OK)
	public TokenResponceBody postRefreshHandler(@RequestBody RefreshTokenCarrier refreshCarrier) {
		JwtService.JwtTokenContainer accessTokenContainer = jwtService
				.generateAccessTokenFromRefreshToken(refreshCarrier.refreshToken());
		return new TokenResponceBody(accessTokenContainer.token(), accessTokenContainer.expiration(),
				refreshCarrier.refreshToken());

	}

	@GetMapping("/login/test")
	public String getLoginTestHandler() {
		return "success";
	}
}

record LoginData(@JsonProperty(required = true) String username, @JsonProperty(required = true) String password) {
}

record RefreshTokenCarrier(@JsonProperty(value = "refresh_token", required = true) String refreshToken) {
}

record TokenResponceBody(@JsonProperty(value = "access_token", required = true) String accessToken,
		@JsonProperty("token_type") String tokenType, @JsonProperty(value = "expires_in") long expiresIn,
		@JsonProperty(value = "refresh_token", required = true) String refreshToken) {

	TokenResponceBody(String accessToken, long expiresIn, String refreshToken) {
		this(accessToken, "Bearer", expiresIn, refreshToken);
	}
}

record ResponceBody(int status, String statusString, String message) {
}
