package com.pravin.shopping_cart.services;

import com.pravin.shopping_cart.config.JwtConfig;
import com.pravin.shopping_cart.entities.Role;
import com.pravin.shopping_cart.entities.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;

@AllArgsConstructor
@Service
public class JwtService {

    private final JwtConfig jwtConfig;

//    @PostConstruct
//    public void init() {
//        System.out.println("JWT Secret = " + secret);
//    }

    public Jwt generateAccessToken(User user){
        return generateToken(user, jwtConfig.getAccessTokenExpiration());
    }

    public Jwt generateRefreshToken(User user){
        return generateToken(user, jwtConfig.getRefreshTokenExpiration());
    }

    private Jwt generateToken(User user, long tokenExpiration) {
        var claims = Jwts.claims()
                .subject(user.getId().toString())
                .add("email", user.getEmail())
                .add("name", user.getName())
                .add("role", user.getRole())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + (tokenExpiration * 1000)))
                .build();

        return new Jwt(claims, jwtConfig.getSecretKey());
    }

//    public boolean validateToken(String token){
//        try{
//            var claims = getClaims(token);
//
//            return claims.getExpiration().after(new Date());
//        }catch (JwtException ex){
//            return false;
//        }
//    }

    public Jwt parseToken(String token){
        try{
            var claims = getClaims(token);
            return new Jwt(claims, jwtConfig.getSecretKey());
        }catch (JwtException e){
            return null;
        }
    }

    private Claims getClaims(String token) {
        var claims = Jwts.parser()
                .verifyWith(jwtConfig.getSecretKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
        return claims;
    }

//    public Long getUserIdFromToken(String token){
//        return Long.valueOf(getClaims(token).getSubject());
//    }

//    public Role getRoleFromToken(String token){
//        return Role.valueOf(getClaims(token).get("role", String.class));
//    }
}
