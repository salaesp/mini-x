package com.example.minix.infrastructure.in.web.filter;

import com.example.minix.config.AuthFilterProperties;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

import static com.example.minix.infrastructure.in.web.ControllerAuthConstants.USER_ID_HEADER;

/**
 * Filter to ensure requests have the required authentication header.
 */
@Component
@Order(1)
public class AuthenticationFilter extends OncePerRequestFilter {

    private final AuthFilterProperties authFilterProperties;

    public AuthenticationFilter(AuthFilterProperties authFilterProperties) {
        this.authFilterProperties = authFilterProperties;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String path = request.getRequestURI();
        if (isExcluded(path)) {
            filterChain.doFilter(request, response);
            return;
        }

        String userId = request.getHeader(USER_ID_HEADER);
        if (userId == null || userId.trim().isEmpty()) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Missing or empty " + USER_ID_HEADER + " header");
            return;
        }

        filterChain.doFilter(request, response);
    }

    private boolean isExcluded(String path) {
        return authFilterProperties.excludedPaths().stream().anyMatch(path::startsWith);
    }
}
