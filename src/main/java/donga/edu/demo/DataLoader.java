package donga.edu.demo;

import donga.edu.demo.models.User;
import donga.edu.demo.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
public class DataLoader {

    @Bean
    CommandLineRunner init(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
        return args -> {
            // Kiểm tra nếu chưa có admin thì tạo
            if(userRepository.findByEmail("admin@example.com").isEmpty()) {
                User admin = new User();
                admin.setName("Admin");
                admin.setEmail("admin@example.com");
                admin.setPassword(passwordEncoder.encode("123456")); // Mật khẩu: 123456
                admin.setRole("ROLE_ADMIN"); // nhớ phải đặt đúng prefix ROLE_
                userRepository.save(admin);
            }
        };
    }
}
