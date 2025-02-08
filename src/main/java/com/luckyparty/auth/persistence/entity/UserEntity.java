package com.luckyparty.auth.persistence.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Comment;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@ToString
@Table(name = "user_info")
public class UserEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_seq")
    @Comment("사용자 시퀀스")
    private Long id;

    @Column(name = "email", length = 200)
    @Comment("사용자 이메일")
    private String email;

    @Column(name = "password", length = 300)
    @Comment("사용자 비밀번호")
    private String password;

    @Column(name = "nickname", length = 50)
    @Comment("사용자 닉네임")
    private String nickname;

    @Column(name = "role", length = 10)
    @Comment("사용자 권한")
    private String role;
}
