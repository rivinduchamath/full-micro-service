package com.cog.gateway.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.io.Encoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.security.Key;
import java.util.Date;

@Component
public class JWTUtil {

    @Value("${jwt.secret}")
    private String secret;

    //@TODO Add base 64 Encrypted Key and here decode it
//    private Key key; // Use this Key Instead of secret
    //    @PostConstruct
//    public void initKey() {
//        byte[] bytes = Decoders.BASE64.decode(Encoders.BASE64.encode(secret.getBytes()));
//
//        this.key =  Keys.hmacShaKeyFor(bytes);
//    }
    public Claims getALlClaims(String token) {
        return Jwts.parserBuilder().setSigningKey(secret).build().parseClaimsJws(token).getBody();
    }

    private boolean isTokenExpired(String token) {
        return this.getALlClaims(token).getExpiration().before(new Date());
    }

    public boolean isInvalid(String token) {
        return this.isTokenExpired(token);
    }

}
