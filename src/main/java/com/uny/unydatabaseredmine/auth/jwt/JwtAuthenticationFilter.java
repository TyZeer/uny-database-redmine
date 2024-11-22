package com.proj.recipe.jwt;

import com.proj.recipe.service.CustomerUserDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtils jwtUtils;
    private final CustomerUserDetailsService customerUserDetailsService;

    @Override
    protected void doFilterInternal(@NotNull HttpServletRequest request,
                                    @NotNull HttpServletResponse response,
                                    @NotNull FilterChain filterChain) throws ServletException, IOException {
        String token = jwtUtils.getToken(request);
        if (token!=null && jwtUtils.validateToken(token)){
            String email = jwtUtils.extractUsername(token);
            UserDetails userDetails = customerUserDetailsService.loadUserByUsername(email);
            if (userDetails!=null){
                UsernamePasswordAuthenticationToken authenticationToken =
                        new UsernamePasswordAuthenticationToken(userDetails.getUsername(),null,userDetails.getAuthorities());
                log.info("authenticated user with username :{}", email);
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
        }
        filterChain.doFilter(request,response);
    }
}
