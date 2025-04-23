package ru.hackathon.finmonitor.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.hackathon.finmonitor.dto.auth.LoginRequest;
import ru.hackathon.finmonitor.dto.auth.LoginResponse;
import ru.hackathon.finmonitor.dto.auth.RegisterRequest;
import ru.hackathon.finmonitor.dto.auth.RegisterResponse;
import ru.hackathon.finmonitor.security.JwtUtils;
import ru.hackathon.finmonitor.security.UserDetailsImpl;
import ru.hackathon.finmonitor.service.AuthService;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final AuthService authService;


    @Operation(summary = "Авторизация пользователя", description = "Процесс аутентификации пользователя с использованием логина и пароля. Возвращает JWT токен.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Успешная авторизация, возвращён JWT токен"),
            @ApiResponse(responseCode = "401", description = "Неверный логин или пароль")
    })
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        String role = userDetails.getAuthorities().stream()
                .findFirst()
                .map(GrantedAuthority::getAuthority)
                .orElse("");

        return ResponseEntity.ok(new LoginResponse(jwt, userDetails.getUsername(), role));
    }

    @Operation(summary = "Регистрация нового пользователя", description = "Создание нового пользователя в системе. Возвращает информацию о зарегистрированном пользователе.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Пользователь успешно зарегистрирован"),
            @ApiResponse(responseCode = "400", description = "Некорректные данные для регистрации")
    })
    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> register(@RequestBody RegisterRequest request) {
        return ResponseEntity.ok(authService.register(request));
    }
} 