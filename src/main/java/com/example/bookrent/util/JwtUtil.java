package com.example.bookrent.util;


import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtUtil {

    private final String secret;

    @Autowired
    public JwtUtil(@Value("${jwt.secret}") String secret) {
        this.secret = secret;
    }


    public String generateToken(String email) {

        try {

            return Jwts.builder()
                    .setId(UUID.randomUUID().toString())
                    .setIssuer("BookRent")
                    .setAudience("user")
                    .setSubject(email)
                    .claim("purpose", "loginToken")
                    .setExpiration(new Date(System.currentTimeMillis() + TimeUnit.DAYS.toMillis(1)))
                    .signWith(SignatureAlgorithm.HS256, secret)
                    .compact();
        } catch (JwtException e) {
            //추후 예외추가
            return null;
        }

    }

    public String getEmailFromToken(String token) {
        try {
            return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody().getSubject();
        } catch (JwtException e) {
            return null;
        }
    }


    public static String extractToken(HttpServletRequest request) {

        String token = getAuthorization(request);

        if (token != null && token.startsWith("Bearer ")) {
            return token.substring(7); // "Bearer " 접두사를 제거합니다.
        }
        return null;

    }


    private static String getAuthorization(HttpServletRequest request) {
        return request.getHeader("Authorization");
    }


}
