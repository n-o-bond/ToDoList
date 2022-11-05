package com.softserve.itacademy.config;

import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import io.jsonwebtoken.Jwts;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class JwtProvider {
    @Value("$(jwt.secret)")
    private String jwtSecret;

    public String generateToken(String login)
    {
        Date date = Date.from(LocalDate.now().plusDays(15).atStartOfDay(ZoneId.systemDefault()).toInstant());
        return Jwts.builder()
                .setSubject(login)
                .setExpiration(date)
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();
    }
    public boolean validateToken(String token)
    {
        try{
            Jwts.parser()
                    .setSigningKey(jwtSecret)
                    .parseClaimsJws(token);
            return true;
        }
        catch (Exception e)
        {
            log.error("invalid token");
        }
        return false;
    }

    public String getLoginFromToken(String token) {
        return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody().getSubject();
    }

    public Date getExpirationDateFromToken(String token) {
        return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody().getExpiration();
    }
}
