package com.neo4j_ecom.demo.security;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.neo4j_ecom.demo.model.dto.response.ApiResponse;
import com.neo4j_ecom.demo.service.impl.UserDetailsServiceImpl;
import com.neo4j_ecom.demo.utils.enums.ErrorCode;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class AuthTokenFilter extends OncePerRequestFilter {
    @Autowired
    private JwtUtil jwtUtils;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    private static final Logger logger = LoggerFactory.getLogger(AuthTokenFilter.class);

    private boolean isByPassToken(HttpServletRequest request) {
        logger.info("By pass token: " + request.getServletPath());
        List<Pair<String, String>> byPassTokens = Arrays.asList(
                Pair.of("/api/v1/auth/login", "POST"),
                Pair.of("/api/v1/auth/register", "POST"),
                Pair.of("/api/v1/auth/refresh-token", "GET"),
                Pair.of("/api/v1/auth/logout", "POST"),
                Pair.of("/api/v1/products", "GET"),
                Pair.of("/api/v1/products/.*", "GET"),
                Pair.of("/api/v1/products/banners", ".*"),
                Pair.of("/api/v1/categories/top-selling", "GET"),
                Pair.of("/api/v1/categories/featured/products?.*", "GET"),
                Pair.of("/api/v1/files/upload", "POST"),
                Pair.of("/api/v1/files/delete", "DELETE")
        );

        return byPassTokens.stream()
                .anyMatch(token -> request.getServletPath().matches(token.getFirst()) &&
                        request.getMethod().equals(token.getSecond()));
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        try {

            if (isByPassToken(request)) {
                filterChain.doFilter(request, response);
                return;
            }

            String jwt = parseJwt(request);
             if (jwt != null && jwtUtils.validateJwtToken(jwt)) {
                String username = jwtUtils.getUserNameFromJwtToken(jwt);

                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);
                filterChain.doFilter(request, response);
                return;
            }

            this.sendUnauthorizedResponse(response);

        } catch (Exception e) {
            this.sendUnauthorizedResponse(response);
        }

    }

    private void sendUnauthorizedResponse(HttpServletResponse response) throws IOException {

        ErrorCode errorCode = ErrorCode.UNAUTHORIZED;

        ApiResponse apiResponse = new ApiResponse(errorCode.getCode(), errorCode.getMessage(), null);
        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.writeValue(response.getWriter(), apiResponse);
    }

    private String parseJwt(HttpServletRequest request) {
        String headerAuth = request.getHeader("Authorization");
        if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
            return headerAuth.substring(7);
        }
        return null;
    }
}