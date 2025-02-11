package com.luckyparty.auth.service.impl;

import com.luckyparty.auth.controller.request.LoginRequest;
import com.luckyparty.auth.controller.request.RefreshRequest;
import com.luckyparty.auth.controller.request.RegisterRequest;
import com.luckyparty.auth.controller.response.UserResponse;
import com.luckyparty.auth.persistence.entity.RefreshTokenEntity;
import com.luckyparty.auth.persistence.entity.UserEntity;
import com.luckyparty.auth.persistence.repository.RefreshEntityRepository;
import com.luckyparty.auth.persistence.repository.UserEntityRepository;
import com.luckyparty.auth.service.AuthService;
import com.luckyparty.common.exception.*;
import com.luckyparty.common.jwt.JwtUtil;
import com.luckyparty.common.jwt.response.JwtResponse;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserEntityRepository userEntityRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final RefreshEntityRepository refreshEntityRepository;

    @Override
    public UserResponse register(RegisterRequest registerRequest) {

        if(userEntityRepository.existsByEmail(registerRequest.getEmail())) {
            throw new AlreadyRegisteredEmailException();
        }

        if(userEntityRepository.existsByNickname(registerRequest.getNickname())) {
            throw new AlreadyRegisteredNicknameException();
        }

        UserEntity user = UserEntity.builder()
                .email(registerRequest.getEmail())
                .nickname(registerRequest.getNickname())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .role("ROLE_USER")
                .build();

        UserEntity saveUser = userEntityRepository.save(user);

        return UserResponse.builder()
                .id(saveUser.getId())
                .nickname(saveUser.getNickname())
                .email(saveUser.getEmail())
                .role(saveUser.getRole())
                .createdAt(saveUser.getCreatedAt())
                .updatedAt(saveUser.getUpdatedAt())
                .build();
    }

    @Override
    @Transactional
    public JwtResponse login(LoginRequest loginRequest) {
        String email = loginRequest.getEmail();
        String password = loginRequest.getPassword();

        UserEntity user = userEntityRepository.findByEmail(email)
                .orElseThrow(UserNotFoundException::new);

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(email, password);
        Authentication authenticate = authenticationManager.authenticate(authenticationToken);

        if(refreshEntityRepository.existsByUserId(user.getId())) {
            refreshEntityRepository.deleteByUserId(user.getId());
        }

        JwtResponse response = jwtUtil.generatedToken(authenticate);
        String refreshToken = response.getRefreshToken();
        Claims claims = jwtUtil.parseToken(refreshToken);

        Date issuedAt = claims.getIssuedAt();
        Date expiration = claims.getExpiration();

        LocalDateTime issued = issuedAt.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        LocalDateTime expired = expiration.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();

        RefreshTokenEntity refresh = RefreshTokenEntity.builder()
                .token(refreshToken)
                .useYn("Y")
                .issuedAt(issued)
                .expiredAt(expired)
                .userId(user.getId())
                .build();

        refreshEntityRepository.save(refresh);

        return response;
    }

    @Override
    @Transactional
    public JwtResponse refresh(RefreshRequest refreshRequest) {
        String refreshToken = refreshRequest.getRefreshToken();

        RefreshTokenEntity refreshTokenEntity = refreshEntityRepository.findByToken(refreshToken)
                .orElseThrow(TokenNotFoundException::new);

        LocalDateTime expiredAt = refreshTokenEntity.getExpiredAt();
        if(LocalDateTime.now().isAfter(expiredAt) || refreshTokenEntity.getUseYn().equals("N")) {
            throw new AccessDeniedException("만료된 토큰입니다.");
        }

        Authentication authentication = jwtUtil.getAuthentication(refreshToken);
        JwtResponse response = jwtUtil.generatedToken(authentication);

        Claims claims = jwtUtil.parseToken(refreshToken);
        Long id = claims.get("id", Long.class);

        UserEntity user = userEntityRepository.findById(id)
                .orElseThrow(UserNotFoundException::new);

        if(refreshEntityRepository.existsByUserId(id)){
            refreshEntityRepository.deleteByUserId(id);
        }

        String newRefreshToken = response.getRefreshToken();
        Claims newClaims = jwtUtil.parseToken(newRefreshToken);

        Date newExpired = newClaims.getExpiration();
        Date newIssued = newClaims.getIssuedAt();

        LocalDateTime expiredLocalDateTime = newExpired.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        LocalDateTime issuedLocalDateTime = newIssued.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();

        RefreshTokenEntity refresh = RefreshTokenEntity.builder()
                .token(newRefreshToken)
                .useYn("Y")
                .expiredAt(expiredLocalDateTime)
                .issuedAt(issuedLocalDateTime)
                .userId(user.getId())
                .build();

        refreshEntityRepository.save(refresh);

        return response;
    }
}
