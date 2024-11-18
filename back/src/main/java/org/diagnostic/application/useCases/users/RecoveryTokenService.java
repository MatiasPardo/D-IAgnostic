package org.diagnostic.application.useCases.users;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Random;

@Service
public class RecoveryTokenService {

    @Value("${jwt.secret}")
    private String secretKey;

    public String generateToken(String email, String code) {
        LocalDateTime expiration = LocalDateTime.now().plusMinutes(10); // Caduca en 10 minutos

        return Jwts.builder()
                .setSubject(email)
                .claim("code", code)
                .claim("expiration", expiration.toString())
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    public boolean validateToken(String token, String email, String code) {
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(secretKey)
                    .parseClaimsJws(token)
                    .getBody();

            String tokenEmail = claims.getSubject();
            String tokenCode = claims.get("code", String.class);
            LocalDateTime expiration = LocalDateTime.parse(claims.get("expiration", String.class));

            // Verifica email, código y expiración
            return tokenEmail.equals(email) && tokenCode.equals(code) && expiration.isAfter(LocalDateTime.now());
        } catch (Exception e) {
            return false; // Token inválido o manipulado
        }
    }

    public String generateOneTimeCode() {
        Random random = new Random();
        int code = 100000 + random.nextInt(900000); // Código de 6 dígitos
        return String.valueOf(code);
    }

}
