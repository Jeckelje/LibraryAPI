package com.modsen.bookstorageservice.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.modsen.bookstorageservice.dto.error.AppError;
import com.modsen.bookstorageservice.exception.ResourceNotFoundException;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;
import java.time.LocalDateTime;

@AllArgsConstructor
public class JwtTokenFilter extends GenericFilterBean {
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse,
                         FilterChain filterChain) throws IOException, ServletException {
        String bearerToken = ((HttpServletRequest) servletRequest).getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            bearerToken = bearerToken.substring(7);

        }

        try {
            if (bearerToken != null && jwtTokenProvider.validateToken(bearerToken)) {
                try {
                    Authentication authentication = jwtTokenProvider.getAuthentication(bearerToken);
                    if (authentication != null) {
                        SecurityContextHolder.getContext().setAuthentication(authentication);
                    }
                } catch (ResourceNotFoundException ignored) {

                }
            }
        } catch (Exception ex) {
            exceptionHandler((HttpServletResponse) servletResponse, ex);
            return;
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }

    private void exceptionHandler(HttpServletResponse response, Exception ex) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        ObjectMapper objectMapper = new ObjectMapper();
        AppError appError;
        if (ex instanceof ExpiredJwtException) {
            appError = new AppError(401, "Expired JWT", LocalDateTime.now());

        } else {
            appError = new AppError(401, "Invalid JWT", LocalDateTime.now());
        }
        response.getWriter().write(objectMapper.writeValueAsString(appError));
    }
}



