package com.uny.unydatabaseredmine.auth.jwt;


import com.uny.unydatabaseredmine.auth.models.Employee;
import com.uny.unydatabaseredmine.auth.service.CustomUserDetailsService;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtUtils {
  private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);

  @Value("${app.jwtSecret}")
  private String jwtSecret;

  @Value("${app.jwtExpirationMs}")
  private int jwtExpirationMs;

  public String generateJwtToken(Authentication authentication) {
    Employee userPrincipal = (Employee) authentication.getPrincipal();
    Map<String, Object> claims = new HashMap<>();
    claims.put("user_id", userPrincipal.getId()); // здесь добавляю user_id

    return Jwts.builder()
            .setSubject(userPrincipal.getUsername())
            .addClaims(claims)
            .setIssuedAt(new Date())
            .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
            .signWith(key(), SignatureAlgorithm.HS256)
            .compact();
  }



  private Key key() {
    return Keys.hmacShaKeyFor(Decoders.BASE64URL.decode(jwtSecret));
  }

  public String getUserNameFromJwtToken(String token) {
    return Jwts.parserBuilder().setSigningKey(key()).build()
               .parseClaimsJws(token).getBody().getSubject();
  }

  public Long getUserIdFromJwtToken(String token) {
    Claims claims = Jwts.parserBuilder().setSigningKey(key()).build().parseClaimsJws(token).getBody();
    return claims.get("user_id", Long.class);
  }



  public boolean validateJwtToken(String authToken) {
    try {
      Jwts.parserBuilder().setSigningKey(key()).build().parse(authToken);
      return true;
    } catch (MalformedJwtException e) {
      logger.error("Недопустимый токен JWT: {}", e.getMessage());
    } catch (ExpiredJwtException e) {
      logger.error("Срок действия токена JWT истек: {}", e.getMessage());
    } catch (UnsupportedJwtException e) {
      logger.error("JWT токен не поддерживается: {}", e.getMessage());
    } catch (IllegalArgumentException e) {
      logger.error("JWT утверждает, что строка пуста: {}", e.getMessage());
    }

    return false;
  }
}
