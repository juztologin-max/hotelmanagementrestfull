package com.hma.api.admin;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.hma.api.authentication.JwtService;
import com.hma.api.users.LoginUser;
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
@RequestMapping("/api/admin")
// @CrossOrigin("") //for allowing react ui to access these endpoints
public class AdminController {
    @GetMapping("/test")
    public String getLoginTestHandler() {
        return "success";
    }

}
