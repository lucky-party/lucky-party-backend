package com.luckyparty.auth.persistence.repository;

import com.luckyparty.auth.persistence.entity.RefreshTokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshEntityRepository extends JpaRepository<RefreshTokenEntity, String> {

    boolean existsByUserId(Long id);

    void deleteByUserId(Long id);

    Optional<RefreshTokenEntity> findByToken(String token);
}
