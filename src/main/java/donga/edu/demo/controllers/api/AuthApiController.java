package donga.edu.demo.controllers.api;

import donga.edu.demo.models.User;
import donga.edu.demo.repository.UserRepository;
import donga.edu.demo.security.JwtService;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthApiController {
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthApiController(AuthenticationManager am, JwtService js, UserRepository ur, PasswordEncoder pe) {
        this.authenticationManager = am; this.jwtService = js; this.userRepository = ur; this.passwordEncoder = pe;
    }

    @PostMapping("/register")
    public AuthResponse register(@RequestBody RegisterRequest req) {
        if (userRepository.findByEmail(req.getEmail()).isPresent()) throw new RuntimeException("Email đã tồn tại");
        User u = new User();
        u.setName(req.getName());
        u.setEmail(req.getEmail());
        u.setPassword(passwordEncoder.encode(req.getPassword()));
        u.setRole("ROLE_USER");
        userRepository.save(u);

        var ud = org.springframework.security.core.userdetails.User
                .withUsername(u.getEmail()).password(u.getPassword()).authorities(u.getRole()).build();
        return new AuthResponse(jwtService.generateToken(ud));
    }

    @PostMapping("/login")
    public AuthResponse login(@RequestBody LoginRequest req) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(req.getEmail(), req.getPassword()));
        } catch (AuthenticationException e) { throw new RuntimeException("Sai email hoặc mật khẩu"); }
        var u = userRepository.findByEmail(req.getEmail()).orElseThrow();
        var ud = org.springframework.security.core.userdetails.User
                .withUsername(u.getEmail()).password(u.getPassword()).authorities(u.getRole()).build();
        return new AuthResponse(jwtService.generateToken(ud));
    }

    @Data public static class RegisterRequest { private String name; @Email @NotBlank private String email; @NotBlank private String password; }
    @Data public static class LoginRequest { @Email @NotBlank private String email; @NotBlank private String password; }
    @Data public static class AuthResponse { private final String accessToken; }
}
