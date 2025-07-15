package api.example.hrm_system.Config;

import api.example.hrm_system.user.User;
import api.example.hrm_system.user.UserPrincipal;
import api.example.hrm_system.user.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        try {
            // Skip authentication for public endpoints
            if (isPublicEndpoint(request)) {
                filterChain.doFilter(request, response);
                return;
            }

            // Extract JWT from header
            String jwt = parseJwt(request);
            if (jwt == null) {
                log.debug("No JWT token found in request headers for: {}", request.getRequestURI());
                filterChain.doFilter(request, response);
                return;
            }

            // Validate token and set authentication
            if (StringUtils.hasText(jwt) && jwtUtil.validateToken(jwt)) {
                authenticateUser(jwt, request);
            } else {
                log.debug("Invalid JWT token for: {}", request.getRequestURI());
            }

            filterChain.doFilter(request, response);
        } catch (Exception e) {
            log.error("Authentication error for {}: {}", request.getRequestURI(), e.getMessage());
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.getWriter().write("{\"error\": \"Authentication failed: " + e.getMessage() + "\"}");
        }
    }

    private boolean isPublicEndpoint(HttpServletRequest request) {
        String path = request.getRequestURI();
        String method = request.getMethod();

        // Always allow auth endpoints
        if (path.startsWith("/api/auth")) {
            return true;
        }

        // Allow Swagger endpoints
        if (path.startsWith("/swagger-ui") ||
                path.startsWith("/v3/api-docs") ||
                path.startsWith("/swagger-resources") ||
                path.startsWith("/webjars")) {
            return true;
        }

        // Allow root path and error
        if (path.equals("/") || path.equals("/error")) {
            return true;
        }

        // Allow GET requests to certain public endpoints
        if ("GET".equals(method)) {
            if (path.equals("/api/holidays") ||
                    path.equals("/api/holidays/upcoming") ||
                    path.startsWith("/api/jobs")) {
                return true;
            }
        }

        return false;
    }

    private String parseJwt(HttpServletRequest request) {
        String headerAuth = request.getHeader("Authorization");
        if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
            return headerAuth.substring(7);
        }
        return null;
    }

    private void authenticateUser(String jwt, HttpServletRequest request) {
        try {
            String email = jwtUtil.extractUsername(jwt);

            if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                userRepository.findByEmail(email).ifPresent(user -> {
                    UserPrincipal userPrincipal = new UserPrincipal(user);
                    if (jwtUtil.validateToken(jwt, userPrincipal)) {
                        UsernamePasswordAuthenticationToken authentication =
                                new UsernamePasswordAuthenticationToken(
                                        userPrincipal,
                                        null,
                                        userPrincipal.getAuthorities()
                                );
                        authentication.setDetails(
                                new WebAuthenticationDetailsSource().buildDetails(request)
                        );
                        SecurityContextHolder.getContext().setAuthentication(authentication);
                        log.debug("Authenticated user: {} with roles: {}", email, userPrincipal.getAuthorities());
                    }
                });
            }
        } catch (Exception e) {
            log.error("Error authenticating user: {}", e.getMessage());
        }
    }
}