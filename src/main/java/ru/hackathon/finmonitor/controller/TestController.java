package ru.hackathon.finmonitor.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.hackathon.finmonitor.security.UserDetailsImpl;

@RestController
@RequestMapping("/api/test")
@RequiredArgsConstructor
public class TestController {

    @Operation(summary = "Тестовый эндпоинт для приветствия", description = "Возвращает приветственное сообщение с именем пользователя и его ролью")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Успешно возвращено приветственное сообщение"),
            @ApiResponse(responseCode = "401", description = "Не авторизован")
    })
    @GetMapping("/hello")
    public ResponseEntity<String> hello() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        return ResponseEntity.ok(String.format(
                "Привет, %s! Твоя роль: %s",
                userDetails.getUsername(),
                userDetails.getAuthorities().iterator().next().getAuthority()));
    }
}