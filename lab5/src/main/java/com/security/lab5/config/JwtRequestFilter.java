package com.security.lab5.config;

import com.security.lab5.auth.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.security.lab5.config.SecurityConstants.AUTHORIZATION_COOKIE;
import static com.security.lab5.config.SecurityConstants.ROUTES_WHITE_LIST;

@Component
@RequiredArgsConstructor
public class JwtRequestFilter extends OncePerRequestFilter {

    private final TokenService tokenService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        if (List.of(ROUTES_WHITE_LIST).contains(request.getRequestURI())) {
            chain.doFilter(request, response);
            return;
        }

        final String token = getToken(request);
        if (token == null){
            chain.doFilter(request, response);
            return;
        }

        UsernamePasswordAuthenticationToken authentication = getAuthentication(token);

        SecurityContextHolder.getContext().setAuthentication(authentication);
        chain.doFilter(request, response);
    }

    private String getToken(HttpServletRequest request) {
        String cookieToken = null;
        Cookie[] cookies = request.getCookies();

        if (cookies != null) {
            for (var cookie : cookies) {
                if (cookie.getName().equals(AUTHORIZATION_COOKIE))
                    cookieToken = cookie.getValue();
            }
        }
        return cookieToken;
    }

    private UsernamePasswordAuthenticationToken getAuthentication(String token) {
        if (token != null) {
            String user = tokenService.extractUserid(token);
            if (user != null && !tokenService.isTokenExpired(token)) {
                return new UsernamePasswordAuthenticationToken(user, null, new ArrayList<>());
            }
            return null;
        }
        return null;
    }
}