package com.luckyparty.common.jwt;

import com.luckyparty.auth.persistence.entity.UserEntity;
import com.luckyparty.auth.persistence.repository.UserEntityRepository;
import com.luckyparty.common.custom.CustomUserDetails;
import com.luckyparty.common.exception.AccessDeniedException;
import com.luckyparty.common.exception.ParseTokenException;
import com.luckyparty.common.exception.UserNotFoundException;
import com.luckyparty.common.jwt.response.JwtResponse;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

@Component
public class JwtUtil {

    private final Key key;
    private final UserEntityRepository userEntityRepository;

    private final Long ACCESS_TOKEN_EXPIRED = 1800000L;
    private final Long REFRESH_TOKEN_EXPIRED = 604800000L;

    public JwtUtil(
            @Value("${jwt.secret}") String secret,
            UserEntityRepository userEntityRepository
    ) {
        byte[] bytes = Decoders.BASE64.decode(secret);
        this.key = Keys.hmacShaKeyFor(bytes);
        this.userEntityRepository = userEntityRepository;
    }

    public JwtResponse generatedToken(Authentication authentication) {
        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        String email = authentication.getName();
        UserEntity user = userEntityRepository.findByEmail(email)
                .orElseThrow(UserNotFoundException::new);

        Date now = new Date();
        long time = now.getTime();
        String accessToken = Jwts.builder()
                .setSubject("access")
                .claim("auth", authorities)
                .claim("id", user.getId())
                .setIssuedAt(now)
                .setExpiration(new Date(time + ACCESS_TOKEN_EXPIRED))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        String refreshToken = Jwts.builder()
                .setSubject("refresh")
                .claim("auth", authorities)
                .claim("id", user.getId())
                .setIssuedAt(now)
                .setExpiration(new Date(time + REFRESH_TOKEN_EXPIRED))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        return JwtResponse.builder()
                .type("bearer")
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .accessTokenExpired(ACCESS_TOKEN_EXPIRED)
                .build();
    }

    public Authentication getAuthentication(String token){
        Claims claims = parseToken(token);

        if(claims.get("auth") == null){
            throw new AccessDeniedException("권한 정보가 없습니다.");
        }

        Collection<? extends GrantedAuthority> authorities = Arrays.stream(claims.get("auth").toString().split(","))
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());

        Long id = claims.get("id", Long.class);
        UserEntity userEntity = userEntityRepository.findById(id)
                .orElseThrow(UserNotFoundException::new);

        CustomUserDetails customUserDetails = new CustomUserDetails(userEntity);
        return new UsernamePasswordAuthenticationToken(customUserDetails, null, authorities);
    }

    public Claims parseToken(String token) {
        try{
            return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
        }catch (ExpiredJwtException e){
            e.printStackTrace();
            throw new ParseTokenException(e.getMessage());
        }catch (UnsupportedJwtException e){
            e.printStackTrace();
            throw new ParseTokenException(e.getMessage());
        }catch (MalformedJwtException e){
            e.printStackTrace();
            throw new ParseTokenException(e.getMessage());
        }catch (SignatureException e){
            e.printStackTrace();
            throw new ParseTokenException(e.getMessage());
        }catch (IllegalArgumentException e){
            e.printStackTrace();
            throw new ParseTokenException(e.getMessage());
        }catch (Exception e){
            e.printStackTrace();
            throw new ParseTokenException(e.getMessage());
        }
    }

    public boolean validateToken(String token) {
        try{
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }
}
