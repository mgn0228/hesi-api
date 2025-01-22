package com.test.back.hesi.utils;

import com.test.back.hesi.web.model.entity.Users;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.Objects;

@Component
public class BfJwtUtil {

    @Value("${encrypt.jwt.key}")
    public String jwtKey;

//    public final static long ACCESS_TOKEN_ALIVE_TIME = (1000L * 5); // 5초
    public final static long ACCESS_TOKEN_ALIVE_TIME = (1000L * 60 * 60) * 12; // 12시간

    public final static long REFRESH_TOKEN_ALIVE_TIME = (1000L * 60 * 60); // 1시간

    private Key getSigningKey(String secretKey) {
        byte[] keyBytes = secretKey.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String generateToken(Users user, long expiredTime) {
        Claims clm = Jwts.claims();
        clm.put("id", user.getUserId());
        clm.put("s", user.getId());
        clm.put("m", user.getEmail());

        return Jwts.builder()
            .setClaims(clm)
            .setIssuedAt(new Date(System.currentTimeMillis()))
            .setExpiration(new Date(System.currentTimeMillis() + expiredTime))
            .signWith(getSigningKey(jwtKey), SignatureAlgorithm.HS512)
            .compact();
    }

    public String generateRefreshToken(Users user, long expiredTime) {
        Claims clm = Jwts.claims();
        clm.put("sem", user.getUserId());
        clm.put("lt", expiredTime);
        clm.put("dm", REFRESH_TOKEN_ALIVE_TIME);

        return Jwts.builder()
            .setClaims(clm)
            .setIssuedAt(new Date(System.currentTimeMillis()))
            .setExpiration(new Date(System.currentTimeMillis() + expiredTime))
            .signWith(getSigningKey(jwtKey), SignatureAlgorithm.HS512)
            .compact();
    }

    public String generateAccessToken(Users user) {
        return generateToken(user, ACCESS_TOKEN_ALIVE_TIME);
    }

    public String generateRefreshToken(Users user) {
        return generateRefreshToken(user, REFRESH_TOKEN_ALIVE_TIME);
    }

    public boolean isExpired(String token) {
        final Claims claims = extractAllClaims(token);

        if (Objects.isNull(claims) || Objects.isNull(claims.getExpiration())) {
            return true;
        }
        return claims.getExpiration().before(new Date());
    }

    public String getUserId(String token) {
        final Claims claims = extractAllClaims(token);
        return (String)claims.get("id");
    }

    public String getUserIdByRefresh(String token) {
        final Claims claims = extractAllClaims(token);
        return (String)claims.get("sem");
    }

    private Claims extractAllClaims(String token) throws ExpiredJwtException {
        return Jwts.parserBuilder()
            .setSigningKey(getSigningKey(jwtKey))
            .build()
            .parseClaimsJws(token)
            .getBody();
    }
}
