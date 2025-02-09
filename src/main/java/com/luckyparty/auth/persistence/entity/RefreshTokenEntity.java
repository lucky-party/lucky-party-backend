package com.luckyparty.auth.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;
import org.hibernate.annotations.Comment;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@Table(name = "refresh_token")
@ToString
public class RefreshTokenEntity {

    @Id
    @Column(name = "token", length = 500)
    @Comment("토큰")
    private String token;

    @Column(name = "use_yn", length = 10)
    @Comment("사용 여부")
    private String useYn;

    @Column(name = "user_seq")
    @Comment("사용자 시퀀스")
    private Long userId;

    @Column(name = "issued_at")
    @Comment("생성 일시")
    private LocalDateTime issuedAt;

    @Column(name = "expired_at")
    @Comment("만료 일시")
    private LocalDateTime expiredAt;
}
