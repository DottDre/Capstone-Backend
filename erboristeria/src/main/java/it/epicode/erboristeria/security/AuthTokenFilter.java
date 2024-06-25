package it.epicode.erboristeria.security;

import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
public class AuthTokenFilter extends OncePerRequestFilter{
    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    ApplicationUserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            var header = request.getHeader("Authorization");

            if (header != null && header.startsWith("Bearer ")) {
                var token = header.substring(7);
                if (!jwtUtils.isTokenValid(token))
                    throw new JwtException("Token non valido");

                var username = jwtUtils.getUsernameFromToken(token);
                var userDetails = userDetailsService.loadUserByUsername(username);

                var authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (Exception e) {
            log.error("Exception in auth filter", e);
        }

        filterChain.doFilter(request, response);
    }
}

