package com.debuggeandoideas.customer_manager.helpers;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Clase para ayudarnos a obtener informacion de los JWT
 */
@Component
public class JwtHelper {

    @Value("${jwt.secret:mySecretKey12345678901234567890123456789012}")
    private String secret;

    @Value("${jwt.expiration:86400000}")
    private Long expiration;

    public String generateJwt(String username, List<String> roles) {
        Date now = new Date();
        Date expirationDate = new Date(now.getTime() + expiration);

        return Jwts.builder()
                .subject(username)
                .issuedAt(now)
                .expiration(expirationDate)
                .claims(Map.of("roles",roles))
                .signWith(Keys.hmacShaKeyFor(secret.getBytes()))
                .compact();
    }

    @SuppressWarnings("unchecked")
    public List<String> getRolesFromJwt(String jwt) {
        Claims claims = this.getClaimsFromJwt(jwt);

        return (List<String>) claims.get("roles");
    }

    public boolean validateJwt(String jwt) {

        try {
            final Claims claims = this.getClaimsFromJwt(jwt);
            final Date expirationDate = claims.getExpiration();

            return expirationDate.before(new Date());
        } catch (Exception e) {
            return false;
        }
    }

    public String getUsernameFromJwt(String jwt) {
        return this.getClaimsFromJwt(jwt).getSubject();
    }

    private SecretKey getSecretKey() {
        return Keys.hmacShaKeyFor(secret.getBytes());
    }

    private Claims getClaimsFromJwt(String jwt) {
        return Jwts.parser()
                .verifyWith(this.getSecretKey())
                .build()
                .parseSignedClaims(jwt)
                .getPayload();
    }
}
