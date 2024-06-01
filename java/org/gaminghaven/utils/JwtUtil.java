package org.gaminghaven.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.stereotype.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Date;

@Component
public class JwtUtil {
    private Dotenv dotenv;

    public JwtUtil() {
        dotenv = Dotenv.configure().load();
    }

    public String generateToken(String email) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("email", email); // Add the email to the payload
        return createToken(claims, email);
    }


    public String createToken(Map<String, Object> claims, String subject) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10)) // Token valid for 10 hours
                .signWith(SignatureAlgorithm.HS256, dotenv.get("JWT_SECRET_KEY"))
                .compact();
    }

    public Claims extractClaims(String token) {
        return Jwts.parser().setSigningKey(dotenv.get("JWT_SECRET_KEY")).parseClaimsJws(token).getBody();
    }
}
