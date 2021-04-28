package com.wip.bool.jwt;

import com.wip.bool.domain.user.CustomUser;
import com.wip.bool.domain.user.Role;
import com.wip.bool.domain.user.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Base64;
import java.util.Date;
import java.util.Map;
import java.util.Optional;

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

    public boolean isValidateToken(String token) {
        String sub = String.valueOf(getBodyFromToken(token).get("sub"));
        if(!StringUtils.isEmpty(sub)) {
            return true;
        }

        return false;
    }

    public boolean isExpireToken(String token) {

        long exp = (long) getBodyFromToken(token).get("exp");
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime expire = LocalDateTime.ofInstant(Instant.ofEpochMilli(exp), ZoneId.systemDefault());

        return now.isAfter(expire);
    }

    public Map<String, Object> getBodyFromToken(String token) {
        return Jwts.parser().setSigningKey(SECURITY_KEY).parseClaimsJws(token).getBody();
    }

    public String getTokenFromHeader(String header) {
        return header.split(" ")[1];
    }

    public Role getRoleFromToken(String token) {
        Map<String, Object> body = getBodyFromToken(token);

        return Optional.ofNullable(body.get("role"))
                .map(u -> (Role) u)
                .orElseThrow(()-> new IllegalArgumentException("유효하지 않은 token입니다. "));
    }

    //JWT 토큰 생성
    public <T> String createToken(T userDetails) {

        String email = "";
        Role role = null;

        if(userDetails instanceof CustomUser) {
            User user = ((CustomUser) userDetails).getUser();
            email = user.getEmail();
            role = user.getRole();
        }

        if(userDetails instanceof DefaultOAuth2User) {
            DefaultOAuth2User user = ((DefaultOAuth2User) userDetails);
            email = String.valueOf(user.getAttributes().get("email"));
            role = user.getAuthorities().stream().findFirst()
                    .map(auth -> Role.valueOf(auth.getAuthority()))
                    .orElseThrow(()-> new IllegalArgumentException());

        }

        Claims claims = Jwts.claims().setSubject(email);
        claims.put("role", role);
        Date now = new Date();

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now) //토큰 발행일자
                .setExpiration(new Date(now.getTime() + tokenValidMilisecond))
                .signWith(SignatureAlgorithm.HS256, SECURITY_KEY)
                .compact();
    }
}
