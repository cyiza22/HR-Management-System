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
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtFilter jwtFilter;
    private final UserDetailsService userDetailsService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        // Public endpoints - no authentication required
                        .requestMatchers(
                                "/api/auth/**",
                                "/api/auth/password-reset/**",
                                "/swagger-ui/**",
                                "/v3/api-docs/**",
                                "/swagger-resources/**",
                                "/webjars/**",
                                "/",
                                "/error"
                        ).permitAll()

                        // Public read-only endpoints
                        .requestMatchers(
                                "/api/holidays",
                                "/api/holidays/upcoming",
                                "/api/jobs",
                                "/api/jobs/*",
                                "/api/projects",
                                "/api/projects/*",
                                "/api/departments"
                        ).permitAll()

                        // File upload endpoint
                        .requestMatchers("/api/upload").authenticated()

                        // HR-only endpoints
                        .requestMatchers(
                                "/api/candidates/**",
                                "/api/holidays",
                                "/api/holidays/*",
                                "/api/departments",
                                "/api/departments/*/",
                                "/api/jobs",
                                "/api/jobs/*",
                                "/api/payroll",
                                "/api/payroll/*"
                        ).hasAuthority("HR")

                        // Manager and HR endpoints
                        .requestMatchers(
                                "/api/candidates",
                                "/api/departments/*",
                                "/api/employees/department/*",
                                "/api/leaves/pending",
                                "/api/leaves/*/approve",
                                "/api/leaves/*/reject",
                                "/api/projects",
                                "/api/projects/assign",
                                "/api/projects/department/*",
                                "/api/attendance/assign",
                                "/api/attendance/department",
                                "/api/payroll/department/*",
                                "/api/payroll/employee/*"
                        ).hasAnyAuthority("MANAGER", "HR")

                        // Employee, Manager, and HR endpoints (personal data access)
                        .requestMatchers(
                                "/api/employees/my-profile",
                                "/api/employees/my-dashboard",
                                "/api/personal-info/my-info",
                                "/api/professional-info/my-info",
                                "/api/account-access/my-account",
                                "/api/leaves",
                                "/api/leaves/my-leaves",
                                "/api/projects/my-projects",
                                "/api/attendance/my-attendance",
                                "/api/payroll/my-payrolls"
                        ).hasAnyAuthority("EMPLOYEE", "MANAGER", "HR")

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

    @Bean
    public UrlBasedCorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOriginPatterns(Arrays.asList("*"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setAllowCredentials(true);
        configuration.setExposedHeaders(Arrays.asList("Authorization"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public CorsFilter corsFilter() {
        return new CorsFilter(corsConfigurationSource());
    }
}