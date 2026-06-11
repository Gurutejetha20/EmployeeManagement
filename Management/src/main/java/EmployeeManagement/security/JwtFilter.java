package EmployeeManagement.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    public JwtFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain chain)
            throws ServletException, IOException {

        String method = request.getMethod();
        String uri    = request.getRequestURI();

        if ("OPTIONS".equalsIgnoreCase(method)) {
            response.setStatus(HttpServletResponse.SC_OK);
            chain.doFilter(request, response);
            return;
        }

        if (uri.startsWith("/auth") || uri.equals("/ping")) {
            chain.doFilter(request, response);
            return;
        }

        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            writeError(response, HttpServletResponse.SC_UNAUTHORIZED,
                    "TOKEN_MISSING",
                    "Authorization token is missing. Please log in.");
            return;
        }

        String token = authHeader.substring(7).trim();

        try {
            if (!jwtUtil.validate(token)) {
                writeError(response, HttpServletResponse.SC_UNAUTHORIZED,
                        "TOKEN_EXPIRED",
                        "Your session has expired. Please log in again.");
                return;
            }
            String username = jwtUtil.extractUsername(token);
            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(
                            username, null,
                            List.of(new SimpleGrantedAuthority("ROLE_USER")));
            SecurityContextHolder.getContext().setAuthentication(authentication);

        } catch (Exception e) {
            writeError(response, HttpServletResponse.SC_UNAUTHORIZED,
                    "UNAUTHORIZED",
                    "Invalid or expired token. Please log in again.");
            return;
        }

        chain.doFilter(request, response);
    }

    private void writeError(HttpServletResponse response, int status,
                            String errorCode, String message) throws IOException {
        response.setStatus(status);
        response.setContentType("application/json");
        response.getWriter().write(
            "{" +
            "\"status\":" + status + "," +
            "\"errorCode\":\"" + errorCode + "\"," +
            "\"message\":\"" + message + "\"," +
            "\"timestamp\":\"" + LocalDateTime.now() + "\"" +
            "}"
        );
    }
}