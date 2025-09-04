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
                        // Cho phép REST API + H2 console + health check (tiện test Postman)
                        .requestMatchers("/api/**", "/h2-console/**", "/actuator/**").permitAll()
                        // H2 console
                        .requestMatchers("/h2-console/**").permitAll()
                        // Trang login, css/js/public
                        .requestMatchers("/login", "/register", "/css/**", "/js/**").permitAll()

                        // Chỉ admin được thêm/sửa/xóa công ty và user
                        .requestMatchers("/companies/new", "/companies/*/edit", "/companies/*/delete").hasRole("ADMIN")
                        .requestMatchers("/users/new", "/users/*/edit", "/users/*/delete").hasRole("ADMIN")

                        // Tất cả user/admin đều có thể xem danh sách và chi tiết
                        .requestMatchers("/companies", "/companies/*").hasAnyRole("ADMIN", "USER")
                        .requestMatchers("/users", "/users/*").hasAnyRole("ADMIN", "USER")

                        // Mặc định cần login
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
