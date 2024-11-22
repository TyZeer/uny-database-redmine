package com.proj.recipe.jwt;


import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.security.Key;
import java.security.KeyFactorySpi;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.function.Function;

@Component
@Slf4j
public class JwtUtils {
    @Value("${app.jwtSecret}")
    private String jwtSecret;
    @Value("${app.jwtExpirationMs}")
    private Long expiration;

    public <T> T extractClaim(String token, Function<Claims,T> claims){
        final  Claims claim = extractAllClaims(token);
        return claims.apply(claim);
    }

    public String extractUsername(String token){
        log.info(extractClaim(token, Claims::getSubject));
        return extractClaim(token,Claims::getSubject);
    }

    public Claims extractAllClaims(String token){
        return Jwts.parserBuilder()
                .setSigningKey(getKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
    public Key getKey(){
        return Keys
                .hmacShaKeyFor(Decoders.BASE64URL.decode(jwtSecret));
    }
    public String generateToken(String email, List<String> roles){
        return Jwts
                .builder()
                .setSubject(email)
                .claim("role",roles)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(Date.from(Instant.now().plus(expiration, ChronoUnit.MICROS)))
                .signWith(getKey(), SignatureAlgorithm.HS256)
                .compact();
    }
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(getKey()).build().parseClaimsJws(token);
            return true;
        } catch (MalformedJwtException e) {
            log.info("Invalid JWT token.");
            log.trace("Invalid JWT token trace: {}", e);
        } catch (ExpiredJwtException e) {
            log.info("Expired JWT token.");
            log.trace("Expired JWT token trace: {}", e);
        } catch (UnsupportedJwtException e) {
            log.info("Unsupported JWT token.");
            log.trace("Unsupported JWT token trace: {}", e);
        } catch (IllegalArgumentException e) {
            log.info("JWT token compact of handler are invalid.");
            log.trace("JWT token compact of handler are invalid trace: {}", e);
        }
        return false;
    }
    public String getToken (HttpServletRequest httpServletRequest) {
        final String bearerToken = httpServletRequest.
                getHeader("Authorization");
        if(StringUtils.hasText(bearerToken) && bearerToken.
                startsWith("Bearer "))
        {return bearerToken.substring(7); } // Thepart after "Bearer "
        return null;
    }
}
