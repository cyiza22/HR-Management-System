package api.example.hrm_system.Config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtFilter jwtFilter;
    private final UserDetailsService userDetailsService;
    private final CorsConfig corsConfig;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(cors -> cors.configurationSource(corsConfig.corsConfigurationSource()))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        // Public endpoints - no authentication required
                        .requestMatchers(
                                "/api/auth/**",
                                "/api/auth/password-reset/**",
                                "/swagger-ui/**",
                                "/swagger-ui.html",
                                "/v3/api-docs/**",
                                "/swagger-resources/**",
                                "/webjars/**",
                                "/",
                                "/error",
                                "/favicon.ico"
                        ).permitAll()

                        // Public read-only endpoints
                        .requestMatchers(
                                "/api/holidays",
                                "/api/holidays/upcoming",
                                "/api/jobs",
                                "/api/jobs/**",
                                "/api/departments"
                        ).permitAll()

                        // File upload - authenticated users only
                        .requestMatchers("/api/upload").authenticated()

                        // HR-only endpoints
                        .requestMatchers(
                                "/api/holidays/**",
                                "/api/departments/**",
                                "/api/jobs/**",
                                "/api/users/**"
                        ).hasAuthority("HR")

                        // HR-only candidate endpoints
                        .requestMatchers("POST", "/api/candidates").hasAuthority("HR")
                        .requestMatchers("PUT", "/api/candidates/*").hasAuthority("HR")
                        .requestMatchers("DELETE", "/api/candidates/*").hasAuthority("HR")

                        // HR-only payroll endpoints
                        .requestMatchers("POST", "/api/payroll").hasAuthority("HR")
                        .requestMatchers("PUT", "/api/payroll/*").hasAuthority("HR")
                        .requestMatchers("PATCH", "/api/payroll/*/status").hasAuthority("HR")
                        .requestMatchers("DELETE", "/api/payroll/*").hasAuthority("HR")

                        // HR-only professional info endpoints
                        .requestMatchers("POST", "/api/professional-info").hasAuthority("HR")
                        .requestMatchers("PUT", "/api/professional-info/*").hasAuthority("HR")
                        .requestMatchers("DELETE", "/api/professional-info/*").hasAuthority("HR")

                        // HR-only personal info endpoints
                        .requestMatchers("DELETE", "/api/personal-info/*").hasAuthority("HR")

                        // HR-only account access endpoints
                        .requestMatchers("POST", "/api/account-access").hasAuthority("HR")
                        .requestMatchers("PUT", "/api/account-access/*").hasAuthority("HR")
                        .requestMatchers("DELETE", "/api/account-access/*").hasAuthority("HR")

                        // Manager and HR endpoints
                        .requestMatchers(
                                "/api/candidates",
                                "/api/leaves/pending",
                                "/api/leaves/*/approve",
                                "/api/leaves/*/reject",
                                "/api/projects/assign",
                                "/api/projects/department/**",
                                "/api/attendance/assign",
                                "/api/attendance/department",
                                "/api/payroll/department/**",
                                "/api/payroll/employee/**",
                                "/api/payroll/export/**"
                        ).hasAnyAuthority("MANAGER", "HR")

                        // Employee, Manager, and HR endpoints - EXPLICIT PATTERNS
                        .requestMatchers(
                                "/api/employees/my-profile",
                                "/api/employees/my-dashboard",
                                "/api/personal-info/my-info",
                                "/api/professional-info/my-info",
                                "/api/account-access/my-account",
                                "/api/leaves",
                                "/api/leaves/my-leaves",
                                "/api/projects/my-projects",
                                "/api/projects", // GET all projects
                                "/api/projects/*", // GET project by ID
                                "/api/attendance/my-attendance",
                                "/api/payroll/my-payrolls",
                                "/api/document/**",
                                "/api/notifications/**"
                        ).hasAnyAuthority("EMPLOYEE", "MANAGER", "HR")

                        // Employee dashboard endpoints for managers and HR
                        .requestMatchers("/api/employees/**").hasAnyAuthority("MANAGER", "HR")

                        // Professional and personal info for managers and HR
                        .requestMatchers("/api/personal-info/**").hasAnyAuthority("MANAGER", "HR")
                        .requestMatchers("/api/professional-info/**").hasAnyAuthority("MANAGER", "HR")
                        .requestMatchers("/api/account-access/**").hasAnyAuthority("MANAGER", "HR")

                        // All other requests require authentication
                        .anyRequest().authenticated()
                )
                .authenticationProvider(authenticationProvider())
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}