package com.example.auth;

import com.example.config.JwtService;
import com.example.entity.Role;
import com.example.entity.User;
import com.example.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationResponse register(RegisterRequest request) {
        var existingUser = repository.findByEmail(request.getEmail());
        if (existingUser.isPresent()) {
            throw new IllegalStateException("Email taken");
        }
        checkIfEmailValid(request.getEmail());

        var user = User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()).toCharArray())
                .role(Role.USER)
                .build();
        repository.save(user);
        var jwtToken = jwtService.generateToken(user);
        user.clearSensitive();
        return AuthenticationResponse.builder().token(jwtToken).build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(),
                        request.getPassword())
        );
        var user = repository.findByEmail(request.getEmail()).orElseThrow();
        var jwtToken = jwtService.generateToken(user);
        user.clearSensitive();
        return AuthenticationResponse.builder().token(jwtToken).build();

    }

    private void checkIfEmailValid(String email) {
        String regex = "^(.+)@(.+)$";

        Pattern pattern = Pattern.compile(regex);
        if (!pattern.matcher(email).matches()) {
            throw new IllegalArgumentException("Email not valid");
        }
    }
}
