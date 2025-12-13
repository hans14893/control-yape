package com.control.yape.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

public class JwtUtil {

    // 🔐 Mínimo 32 caracteres para HS256
    private static final String SECRET = "CAMBIA_ESTA_CLAVE_SUPER_SECRETA_123456";
    private static final long EXP_MS = 1000 * 60 * 60 * 12; // 12 horas

    private static final Key KEY =
            Keys.hmacShaKeyFor(SECRET.getBytes(StandardCharsets.UTF_8));

    public static String generateToken(String username) {
        Date now = new Date();
        Date exp = new Date(now.getTime() + EXP_MS);

        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(now)
                .setExpiration(exp)
                .signWith(KEY, SignatureAlgorithm.HS256)
                .compact();
    }

    public static String getUsername(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(KEY)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }
}
