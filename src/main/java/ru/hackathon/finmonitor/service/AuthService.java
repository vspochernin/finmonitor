package ru.hackathon.finmonitor.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.hackathon.finmonitor.dto.auth.RegisterRequest;
import ru.hackathon.finmonitor.dto.auth.RegisterResponse;
import ru.hackathon.finmonitor.exception.FinmonitorErrorType;
import ru.hackathon.finmonitor.exception.FinmonitorException;
import ru.hackathon.finmonitor.model.User;
import ru.hackathon.finmonitor.model.UserRole;
import ru.hackathon.finmonitor.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public RegisterResponse register(RegisterRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new FinmonitorException(FinmonitorErrorType.LOGIN_EXISTS);
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(UserRole.ROLE_USER);

        userRepository.save(user);

        return new RegisterResponse("Пользователь успешно зарегистрирован", user.getUsername());
    }
} 