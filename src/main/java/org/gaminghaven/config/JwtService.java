package org.gaminghaven.config;


import io.github.cdimascio.dotenv.Dotenv;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.json.JSONObject;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {
    private Dotenv dotenv;

    public JwtService() {
        dotenv = Dotenv.configure().load();
    }

    public String extractEmail(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
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

    public String generateToken(String email) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("email", email); // Add the email to the payload
        return createToken(claims, email);
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String email = extractEmail(token);
        return email.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }

    public boolean googleTokenIsValid(String email, String token) {
        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest httpRequest;
        httpRequest = HttpRequest.newBuilder().
                uri(URI.create("https://oauth2.googleapis.com/tokeninfo?id_token=" + token)).build();
        try {
            HttpResponse<String> response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
            JSONObject googleTokenVerificationBody = new JSONObject(response.body());
            if (response.statusCode() == 200 &&
                    googleTokenVerificationBody.getString("iss").equals("accounts.google.com") &&
                    googleTokenVerificationBody.get("email").equals(email)) {
                return true;
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return false;
        }

        return false; // Default return false if authProvider is not "google" or verification fails
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder().
                setSigningKey(dotenv.get("JWT_SECRET_KEY")).
                build().
                parseClaimsJws(token).
                getBody();

    }
}
