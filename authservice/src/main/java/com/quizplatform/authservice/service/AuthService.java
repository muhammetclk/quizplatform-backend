package com.quizplatform.authservice.service;



import com.quizplatform.authservice.dto.*;
import com.quizplatform.authservice.entity.User;
import com.quizplatform.authservice.repository.UserRepository;
import com.quizplatform.authservice.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail()))
            throw new RuntimeException("Bu email zaten kullanımda");
        if (userRepository.existsByUsername(request.getUsername()))
            throw new RuntimeException("Bu kullanıcı adı zaten kullanımda");

        var user = User.builder()
                .email(request.getEmail())
                .username(request.getUsername())
                .passwordHash(passwordEncoder.encode(request.getPassword()))
                .build();

        userRepository.save(user);

        return buildAuthResponse(user);
    }

    public AuthResponse login(LoginRequest request) {
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        var user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Kullanıcı bulunamadı"));

        return buildAuthResponse(user);
    }

    public void logout(String token) {
        jwtService.blacklistToken(token);
    }

    private AuthResponse buildAuthResponse(User user) {
        return AuthResponse.builder()
                .accessToken(jwtService.generateAccessToken(user.getEmail(), user.getRole().name()))
                .refreshToken(jwtService.generateRefreshToken(user.getEmail()))
                .username(user.getUsername())
                .email(user.getEmail())
                .role(user.getRole().name())
                .build();
    }
}