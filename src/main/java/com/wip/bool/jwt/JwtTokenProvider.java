package com.wip.bool.jwt;

import com.wip.bool.domain.user.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Base64;
import java.util.Date;
import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtTokenProvider {

    /**
     * JWT 토큰 생성 및 유효성을 검증하는 컴포넌트
     */

    @Value("spring.jwt.security")
    private String SECURITY_KEY;

    //Token 만료시간(4시간)
    private long tokenValidMilisecond = 1000L * 60 * 60 * 4;

    @PostConstruct
    protected void init() {
        SECURITY_KEY = Base64.getEncoder().encodeToString(SECURITY_KEY.getBytes());
    }

    public boolean isValidateToken(String jwt) {
        String sub = String.valueOf(getBodyFromToken(jwt).get("sub"));
        if(!StringUtils.isEmpty(sub)) {
            return true;
        }

        return false;
    }

    public boolean isExpireToken(String jwt) {

        long exp = (long) getBodyFromToken(jwt).get("exp");
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime expire = LocalDateTime.ofInstant(Instant.ofEpochMilli(exp), ZoneId.systemDefault());

        return now.isAfter(expire);
    }

    public Map<String, Object> getBodyFromToken(String jwt) {
        return Jwts.parser().setSigningKey(SECURITY_KEY).parseClaimsJws(jwt).getBody();
    }

    public String getTokenFromHeader(String header) {
        return header.split(" ")[1];
    }

    //JWT 토큰 생성
    public String createToken(User user) {
        Claims claims = Jwts.claims().setSubject(String.valueOf(user.getId()));
        claims.put("role", user.getRole());
        Date now = new Date();

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now) //토큰 발행일자
                .setExpiration(new Date(now.getTime() + tokenValidMilisecond))
                .signWith(SignatureAlgorithm.HS256, SECURITY_KEY)
                .compact();
    }
}
