package donga.edu.demo.controllers.api;

import donga.edu.demo.controllers.api.dto.CreateUserReq;
import donga.edu.demo.controllers.api.dto.UpdateUserReq;
import donga.edu.demo.controllers.api.dto.UserDto;
import donga.edu.demo.models.User;
import donga.edu.demo.repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserApiController {

    private final UserRepository repo;
    private final PasswordEncoder encoder;

    public UserApiController(UserRepository repo, PasswordEncoder encoder) {
        this.repo = repo; this.encoder = encoder;
    }

    @GetMapping
    public List<UserDto> all() {
        return repo.findAll().stream().map(UserDto::from).toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> one(@PathVariable Long id) {
        return repo.findById(id).map(UserDto::from)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<UserDto> create(@Valid @RequestBody CreateUserReq req) {
        if (repo.existsByEmail(req.email())) {
            return ResponseEntity.badRequest().build();
        }
        User u = new User();
        u.setName(req.name());
        u.setEmail(req.email());
        u.setPassword(encoder.encode(req.password()));
        u.setRole(req.role() == null || req.role().isBlank() ? "ROLE_USER" : req.role());

        repo.save(u);
        return ResponseEntity.created(URI.create("/api/users/" + u.getId()))
                .body(UserDto.from(u));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDto> update(@PathVariable Long id, @Valid @RequestBody UpdateUserReq req) {
        return repo.findById(id).map(u -> {
            if (req.name() != null) u.setName(req.name());
            if (req.email() != null) u.setEmail(req.email());
            if (req.password() != null && !req.password().isBlank()) {
                u.setPassword(encoder.encode(req.password()));
            }
            if (req.role() != null && !req.role().isBlank()) u.setRole(req.role());
            repo.save(u);
            return ResponseEntity.ok(UserDto.from(u));
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (!repo.existsById(id)) return ResponseEntity.notFound().build();
        repo.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/../me")
    public ResponseEntity<UserDto> me(@AuthenticationPrincipal UserDetails principal) {
        if (principal == null) return ResponseEntity.status(401).build();
        return repo.findByEmail(principal.getUsername())
                .map(UserDto::from).map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
