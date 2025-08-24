package donga.edu.demo.repository;

import donga.edu.demo.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    // Thêm phương thức tìm theo email
    Optional<User> findByEmail(String email);
}
