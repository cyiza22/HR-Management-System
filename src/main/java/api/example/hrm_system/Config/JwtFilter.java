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
import java.util.Enumeration;

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

        String path = request.getRequestURI();
        String method = request.getMethod();

        log.info("Processing request: {} {}", method, path);

        // Log all headers for debugging
        logHeaders(request);

        try {
            // Skip authentication for public endpoints
            if (isPublicEndpoint(request)) {
                log.info("Public endpoint, skipping authentication: {} {}", method, path);
                filterChain.doFilter(request, response);
                return;
            }

            // Extract JWT from header - try multiple ways
            String jwt = parseJwt(request);

            if (jwt == null) {
                log.warn("No JWT token found for protected endpoint: {} {}", method, path);
                log.warn("Authorization header: {}", request.getHeader("Authorization"));
                sendUnauthorizedResponse(response, "Authentication required - no token provided");
                return;
            }

            log.info("JWT token found, length: {}", jwt.length());

            // Validate token and set authentication
            try {
                if (jwtUtil.validateToken(jwt)) {
                    log.info("JWT token is valid, proceeding with authentication");
                    authenticateUser(jwt, request);
                    log.info("User authenticated successfully");
                } else {
                    log.warn("JWT token validation failed");
                    sendUnauthorizedResponse(response, "Invalid or expired token");
                    return;
                }
            } catch (Exception e) {
                log.error("Error during JWT validation: {}", e.getMessage(), e);
                sendUnauthorizedResponse(response, "Token validation failed: " + e.getMessage());
                return;
            }

            filterChain.doFilter(request, response);
        } catch (Exception e) {
            log.error("Unexpected error in JWT filter for {}: {}", path, e.getMessage(), e);
            sendUnauthorizedResponse(response, "Authentication failed: " + e.getMessage());
        }
    }

    private void logHeaders(HttpServletRequest request) {
        log.debug("=== REQUEST HEADERS ===");
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            String headerValue = request.getHeader(headerName);
            if ("authorization".equalsIgnoreCase(headerName)) {
                log.debug("{}: {}", headerName, headerValue != null ? "Bearer ***" : "null");
            } else {
                log.debug("{}: {}", headerName, headerValue);
            }
        }
        log.debug("=====================");
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
                    path.startsWith("/api/jobs") ||
                    path.startsWith("/api/projects") ||
                    path.equals("/api/departments")) {
                return true;
            }
        }

        return false;
    }

    private String parseJwt(HttpServletRequest request) {
        // Try Authorization header first
        String headerAuth = request.getHeader("Authorization");
        log.debug("Authorization header raw: {}", headerAuth);

        if (StringUtils.hasText(headerAuth)) {
            if (headerAuth.startsWith("Bearer ")) {
                String token = headerAuth.substring(7).trim();
                log.debug("Extracted token from Bearer header, length: {}", token.length());
                return token;
            } else if (headerAuth.startsWith("bearer ")) {
                String token = headerAuth.substring(7).trim();
                log.debug("Extracted token from bearer header (lowercase), length: {}", token.length());
                return token;
            } else {
                log.debug("Authorization header doesn't start with Bearer: {}", headerAuth);
            }
        }

        // Try alternative header names that Swagger might use
        String[] alternativeHeaders = {"X-Authorization", "Authentication", "Token"};
        for (String header : alternativeHeaders) {
            String value = request.getHeader(header);
            if (StringUtils.hasText(value)) {
                log.debug("Found token in {} header", header);
                return value.startsWith("Bearer ") ? value.substring(7).trim() : value.trim();
            }
        }

        log.debug("No token found in any header");
        return null;
    }

    private void authenticateUser(String jwt, HttpServletRequest request) {
        try {
            String email = jwtUtil.extractUsername(jwt);
            log.info("Extracted email from JWT: {}", email);

            if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                User user = userRepository.findByEmail(email).orElse(null);
                if (user != null) {
                    log.info("Found user: {} with role: {}, verified: {}",
                            user.getEmail(), user.getRole(), user.isVerified());

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
                        log.info("Authentication successful for user: {} with authorities: {}",
                                email, userPrincipal.getAuthorities());
                    } else {
                        log.warn("JWT token validation failed for user: {}", email);
                        throw new RuntimeException("Token validation failed for user");
                    }
                } else {
                    log.warn("User not found for email: {}", email);
                    throw new RuntimeException("User not found");
                }
            } else {
                if (email == null) {
                    log.warn("Could not extract email from JWT");
                    throw new RuntimeException("Invalid token - no email found");
                }
                if (SecurityContextHolder.getContext().getAuthentication() != null) {
                    log.debug("User already authenticated");
                }
            }
        } catch (Exception e) {
            log.error("Error authenticating user: {}", e.getMessage(), e);
            throw new RuntimeException("Authentication failed", e);
        }
    }

    private void sendUnauthorizedResponse(HttpServletResponse response, String message) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write("{\"error\": \"" + message + "\"}");
        response.getWriter().flush();
    }
}