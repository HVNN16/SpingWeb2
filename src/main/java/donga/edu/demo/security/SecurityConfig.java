package donga.edu.demo.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
public class SecurityConfig {

    private final CustomUserDetailsService userDetailsService;
    private final JwtAuthFilter jwtAuthFilter;
    private final JwtAuthenticationEntryPoint authEntryPoint;

    public SecurityConfig(CustomUserDetailsService userDetailsService,
                          JwtAuthFilter jwtAuthFilter,
                          JwtAuthenticationEntryPoint authEntryPoint) {
        this.userDetailsService = userDetailsService;
        this.jwtAuthFilter = jwtAuthFilter;
        this.authEntryPoint = authEntryPoint;
    }

    /* ===== Common beans ===== */
    @Bean
    public BCryptPasswordEncoder passwordEncoder() { return new BCryptPasswordEncoder(); }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider p = new DaoAuthenticationProvider();
        p.setUserDetailsService(userDetailsService);
        p.setPasswordEncoder(passwordEncoder());
        return p;
    }

    @Bean
<<<<<<< HEAD
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
=======
    public AuthenticationManager authenticationManager(AuthenticationConfiguration cfg) throws Exception {
        return cfg.getAuthenticationManager();
    }

    /* ===== Chain 1: REST API - JWT, stateless ===== */
    @Bean
    @Order(0)
    public SecurityFilterChain apiSecurityFilterChain(HttpSecurity http) throws Exception {
        http
                .securityMatcher(new AntPathRequestMatcher("/api/**"))
                .csrf(csrf -> csrf.disable())
                .cors(cors -> {})
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .exceptionHandling(ex -> ex.authenticationEntryPoint(authEntryPoint))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/auth/**", "/api/public/**").permitAll()
                        .anyRequest().authenticated()
                )
                // TẮT hoàn toàn form-login và HTTP Basic trên API
                .formLogin(form -> form.disable())
                .httpBasic(basic -> basic.disable())
                .authenticationProvider(authenticationProvider())
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    /* ===== Chain 2: Web MVC - Form Login (Thymeleaf) ===== */
    @Bean
    @Order(1)
    public SecurityFilterChain webSecurityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .headers(h -> h.frameOptions(f -> f.disable())) // H2 console
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/h2-console/**").permitAll()
                        .requestMatchers("/login", "/register", "/css/**", "/js/**", "/images/**").permitAll()
                        .requestMatchers("/companies/new", "/companies/*/edit", "/companies/*/delete").hasRole("ADMIN")
                        .requestMatchers("/users/new", "/users/*/edit", "/users/*/delete").hasRole("ADMIN")
                        .requestMatchers("/companies", "/companies/*").hasAnyRole("ADMIN", "USER")
                        .requestMatchers("/users", "/users/*").hasAnyRole("ADMIN", "USER")
>>>>>>> origin/master
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .defaultSuccessUrl("/users", true)
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout")
                        .permitAll()
                )
                .authenticationProvider(authenticationProvider());

        return http.build();
    }
}
