package com.clothingshop.api.security;

import com.clothingshop.api.domain.entities.UserEntity;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.security.core.Authentication;

import java.security.Key;
import java.util.Date;

@Log
@Component
public class JwtUtils {
    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.expiryInMils}")
    private int jwtExpiryInMils;

    public String generateJwtToken(Authentication authentication) {

        UserEntity userPrincipal = (UserEntity) authentication.getPrincipal();

        return generateTokenFromUsername(userPrincipal.getUsername());
    }

    private Key key() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
    }

    public String getUserNameFromJwtToken(String token) {
        return Jwts.parserBuilder().setSigningKey(key()).build()
                .parseClaimsJws(token).getBody().getSubject();
    }

    public boolean validateJwtToken(String authToken) {
        try {
            Jwts.parserBuilder().setSigningKey(key()).build().parse(authToken);
            return true;
        } catch (MalformedJwtException e) {
            log.info("Invalid JWT token: " + e.getMessage());
        } catch (ExpiredJwtException e) {
            log.info("JWT token is expired: " + e.getMessage());
        } catch (UnsupportedJwtException e) {
            log.info("JWT token is unsupported: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            log.info("JWT claims string is empty: " + e.getMessage());
        }

        return false;
    }

    public String generateTokenFromUsername(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + jwtExpiryInMils))
                .signWith(key(), SignatureAlgorithm.HS256)
                .compact();
    }
}
