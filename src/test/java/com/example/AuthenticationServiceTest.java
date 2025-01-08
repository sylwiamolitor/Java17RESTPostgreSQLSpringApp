package com.example;

import com.example.auth.AuthenticationRequest;
import com.example.auth.AuthenticationResponse;
import com.example.auth.AuthenticationService;
import com.example.auth.RegisterRequest;
import com.example.config.JwtService;
import com.example.entity.Role;
import com.example.entity.User;
import com.example.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthenticationServiceTest {

    @Mock
    private UserRepository repository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private AuthenticationService authenticationService;

    @Test
    void testRegister_UserAlreadyExists() {
        RegisterRequest request = new RegisterRequest("John", "Doe", "john@example.com", "password");

        when(repository.findByEmail(request.getEmail())).thenReturn(Optional.of(new User()));

        assertThrows(IllegalStateException.class, () -> authenticationService.register(request));
        verify(repository, never()).save(any());
    }

    @Test
    void testRegister_InvalidEmail() {
        RegisterRequest request = new RegisterRequest("John", "Doe", "invalid-email", "password");

        assertThrows(IllegalArgumentException.class, () -> authenticationService.register(request));
        verify(repository, never()).save(any());
    }

    @Test
    void testRegister_Success() {
        RegisterRequest request = new RegisterRequest("John", "Doe", "john@example.com", "password");
        User user = new User();
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setEmail(request.getEmail());
        user.setPassword("encodedPassword".toCharArray());
        user.setRole(Role.USER);

        when(repository.findByEmail(request.getEmail())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(request.getPassword())).thenReturn("encodedPassword");
        when(repository.save(any(User.class))).thenReturn(user);
        when(jwtService.generateToken(any(User.class))).thenReturn("jwtToken");

        AuthenticationResponse response = authenticationService.register(request);

        assertNotNull(response);
        assertEquals("jwtToken", response.getToken());
        verify(repository).save(any(User.class));
    }

    @Test
    void testAuthenticate_Success() {
        User user = User.builder()
                .ID(1)
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@example.com")
                .password("password".toCharArray())
                .role(Role.USER)
                .build();
        String email = "john.doe@example.com";
        String password = "password";
        AuthenticationRequest request = new AuthenticationRequest(email, password);

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(new UsernamePasswordAuthenticationToken(user, null));
        when(repository.findByEmail(email)).thenReturn(Optional.of(user));
        when(jwtService.generateToken(user)).thenReturn("mockJwtToken");

        AuthenticationResponse response = authenticationService.authenticate(request);

        assertNotNull(response);
        assertEquals("mockJwtToken", response.getToken());
        verify(repository).findByEmail(email);
        verify(jwtService).generateToken(user);
    }

    @Test
    void testAuthenticate_UserNotFound() {
        AuthenticationRequest request = new AuthenticationRequest("john@example.com", "wrongPassword");

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(null);
        when(repository.findByEmail(request.getEmail())).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> authenticationService.authenticate(request));
    }
}
