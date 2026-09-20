package com.hma.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.hma.api.authentication.JwtService;
import com.hma.api.users.LoginUser;
import com.hma.api.users.UserService;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
// @CrossOrigin("") //for allowing react ui to access these endpoints
public class AuthApiController {
    private final AuthenticationManager authManager;
    private final JwtService jwtService;
    private final UserService userService;

    public AuthApiController(AuthenticationManager authenticationManager, JwtService jwtService,
            UserService userService) {
        this.authManager = authenticationManager;
        this.jwtService = jwtService;
        this.userService = userService;
    }

    @PostMapping("/login")
    // @ResponseStatus(HttpStatus.OK) // not needed as default is SUCCESS
    public ResponseEntity<TokenResponseBody> postLoginHandler(@RequestBody LoginData loginData) {
        UsernamePasswordAuthenticationToken unauthenticatedToken = new UsernamePasswordAuthenticationToken(
                loginData.username(), loginData.password(), null);
        Authentication authenticatedToken = authManager.authenticate(unauthenticatedToken);
        LoginUser user = (LoginUser) authenticatedToken.getPrincipal();
        Set<String> roles = user.getRoles().stream().map(r -> r.getRole().name()).collect(Collectors.toSet());
        String refreshToken = jwtService.generateRefreshToken(loginData.username(), roles,
                jwtService.getDefaultRefreshExpiration());
        ResponseCookie responseCookie = ResponseCookie.from("refresh_cookie", refreshToken).httpOnly(true).secure(true)
                .path("/api/auth")
                .maxAge(jwtService.getDefaultRefreshExpiration()).sameSite("Lax").partitioned(true).build();

        JwtService.JwtTokenContainer accessTokenContainer = jwtService.generateAccessToken(loginData.username(),
                roles);
        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, responseCookie.toString())
                .body(new TokenResponseBody(accessTokenContainer.token(), accessTokenContainer.expiration()));

    }

    @PostMapping("/refresh")
    // @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<TokenResponseBody> postRefreshHandler(
            @CookieValue(value = "refresh_cookie", required = false) String refreshCookie) {
        if (refreshCookie == null || refreshCookie.isBlank()) {
            System.out.println("no CookieValue");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        System.out.println(refreshCookie);
        JwtService.JwtTokenContainer accessTokenContainer = jwtService
                .generateAccessTokenFromRefreshToken(refreshCookie);

        return ResponseEntity.ok().body(
                new TokenResponseBody(accessTokenContainer.token(), accessTokenContainer.expiration()));

    }

    @PostMapping("/logout")
    // @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Void> postLogoutHandler() {
        ResponseCookie newRefreshCookie = ResponseCookie.from("refresh_cookie", "").httpOnly(true).secure(true)
                .path("/api/auth")
                .maxAge(0).sameSite("Lax").partitioned(true).build();
        ;
        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, newRefreshCookie.toString()).build();

    }

    @PostMapping("/is-username-available")
    // @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Map<String, Boolean>> postLogoutHandler(@RequestBody Map<String, String> rMap) {
        Map<String, Boolean> response = new HashMap<>();
        response.put(rMap.get("username"), !userService.doesUsernameExist(rMap.get("username")));
        return ResponseEntity.ok().body(response);

    }

    @GetMapping("/login/test")
    public String getLoginTestHandler() {
        return "success";
    }

}

record LoginData(@JsonProperty(value = "username", required = true) String username,
        @JsonProperty(value = "password", required = true) String password) {
}

record TokenResponseBody(@JsonProperty(value = "access_token", required = true) String accessToken,
        @JsonProperty("token_type") String tokenType, @JsonProperty(value = "expires_in") long expiresIn) {

    TokenResponseBody(String accessToken, long expiresIn) {
        this(accessToken, "Bearer", expiresIn);
    }
}

record ResponceBody(int status, String statusString, String message) {
}
