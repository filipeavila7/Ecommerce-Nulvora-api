package com.nulvora.nulvora.auth.controller;



import com.nulvora.nulvora.auth.dto.request.AuthRequest;
import com.nulvora.nulvora.auth.dto.response.TokenResponse;
import com.nulvora.nulvora.auth.service.AuthService;
import com.nulvora.nulvora.refreshToken.dto.request.RefreshRequest;
import com.nulvora.nulvora.user.dto.request.UserRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    // cadastrar usuario
    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public TokenResponse register(
            @Valid @RequestBody UserRequest request
    ) {
        return authService.register(request);
    }

    // realizar login
    @PostMapping("/login")
    public ResponseEntity<TokenResponse>  login(
            @Valid @RequestBody AuthRequest request
    ) {
        return ResponseEntity.ok(authService.login(request));
    }

    // recriar jwt com refreshToken
    @PostMapping("/refresh")
    public TokenResponse refresh(@Valid @RequestBody RefreshRequest request) {
        return authService.refresh(request.refreshToken());
    }

    // realizar logout
    @PostMapping("/logout")
    public ResponseEntity<Void> logout(
            @Valid @RequestBody RefreshRequest request
    ) {
        authService.logout(request.refreshToken());

        return ResponseEntity.noContent().build();
    }
}
