package org.example.task.config.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Value;
import org.example.task.model.request.LoginRequest;
import org.example.task.model.user.Users;
import org.springframework.stereotype.Component;
import java.io.Serializable;
import java.util.*;
import java.util.function.Function;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


@Component
@AllArgsConstructor

public class JwtUtil implements Serializable {
    private final String secret;
    private final Long expirationTime;
    private final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    public JwtUtil() {
        this.secret = "verystrongjwtsecretkeythatishardtotrickyousuperlong";
        this.expirationTime = 3600000L; // 1 hour
    }

    private final long EXPIRATION = 1000 * 60 * 60;

    public String generateToken(Users user) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("id", user.getId());
        claims.put("phoneNumber", user.getPhoneNumber());
        claims.put("chatId", user.getChatId());
        claims.put("gmail", user.getGmail());
        claims.put("roles", user.getRole());
        claims.put("provider", user.getProvider());

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(key)
                .compact();
    }

    public String getUserIdFromToken(String token) {
        Claims claims = getAllClaimsFromToken(token);
        return claims.get("id", String.class);
    }

    public String getPhoneNumberFromToken(String token) {
        Claims claims = getAllClaimsFromToken(token);
        return claims.get("phoneNumber", String.class);
    }

    // Retrieve chat ID from JWT token
    public String getChatIdFromToken(String token) {
        Claims claims = getAllClaimsFromToken(token);
        return claims.get("chatId", String.class);
    }

    // Retrieve Gmail from JWT token
    public String getGmailFromToken(String token) {
        Claims claims = getAllClaimsFromToken(token);
        return claims.get("gmail", String.class);
    }

    // Retrieve roles from JWT token
    public String getRolesFromToken(String token) {
        Claims claims = getAllClaimsFromToken(token);
        return claims.get("role", String.class);
    }

    public String getProviderFromToken(String token) {
        Claims claims = getAllClaimsFromToken(token);
        return claims.get("provider", String.class);
    }

    // Retrieve expiration date from JWT token
    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
    }

    private Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    // Validate token
    public Boolean validateToken(String token, Users user) {
        final String userId = getUserIdFromToken(token);
        final String phoneNumber = getPhoneNumberFromToken(token);
        final String provider = getProviderFromToken(token);

        // Check if the token is expired
        if (isTokenExpired(token)) {
            return false;
        }
        // Validate the username, phone number, and chat ID
        return  provider.equalsIgnoreCase(user.getProvider().toString()) &&
                userId.equalsIgnoreCase(String.valueOf(user.getId())) &&
                phoneNumber.equals(user.getPhoneNumber());
    }

}

