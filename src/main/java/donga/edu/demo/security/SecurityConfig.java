package donga.edu.demo.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    private final CustomUserDetailsService userDetailsService;

    public SecurityConfig(CustomUserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable())
                .headers(headers -> headers.frameOptions().disable()) // cho H2 console
                .authorizeHttpRequests(auth -> auth
                        // ====== API SECURITY RULES ======
                        // /api/users/** chỉ ADMIN
                        .requestMatchers("/api/users/**").hasRole("ADMIN")
                        // /api/me cho tất cả user đăng nhập
                        .requestMatchers("/api/me").authenticated()
                        // Cho phép public API login, register
                        .requestMatchers("/api/auth/**").permitAll()

                        // ====== H2 & Actuator ======
                        .requestMatchers("/h2-console/**", "/actuator/**").permitAll()

                        // ====== Trang web MVC ======
                        // Chỉ admin được thêm/sửa/xóa công ty và user (web UI)
                        .requestMatchers("/companies/new", "/companies/*/edit", "/companies/*/delete").hasRole("ADMIN")
                        .requestMatchers("/users/new", "/users/*/edit", "/users/*/delete").hasRole("ADMIN")

                        // User & Admin đều có thể xem
                        .requestMatchers("/companies", "/companies/*").hasAnyRole("ADMIN", "USER")
                        .requestMatchers("/users", "/users/*").hasAnyRole("ADMIN", "USER")

                        // Trang login, css/js/public
                        .requestMatchers("/login", "/register", "/css/**", "/js/**").permitAll()

                        // Mặc định: cần login
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .defaultSuccessUrl("/users", true)
                        .permitAll()
                )
                .logout(logout -> logout.logoutUrl("/logout").logoutSuccessUrl("/login?logout").permitAll());

        return http.build();
    }

}
