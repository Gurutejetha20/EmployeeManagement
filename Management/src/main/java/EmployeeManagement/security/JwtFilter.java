package EmployeeManagement.security;

import jakarta.servlet.*;
import jakarta.servlet.http.*;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class JwtFilter implements Filter {

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;

        String authHeader = req.getHeader("Authorization");

        // ✅ Allow /auth without token
        if (req.getRequestURI().contains("/auth")) {
            chain.doFilter(request, response);
            return;
        }

        // ✅ Check token
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            ((HttpServletResponse) response).setStatus(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        String token = authHeader.substring(7);

        try {
            jwtUtil.validate(token);
        } catch (Exception e) {
            ((HttpServletResponse) response).setStatus(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        chain.doFilter(request, response);
    }
}