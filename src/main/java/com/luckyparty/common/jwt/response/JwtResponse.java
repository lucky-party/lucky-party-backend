package com.luckyparty.common.jwt.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class JwtResponse {

    private String type;

    private String accessToken;

    private String refreshToken;

    private Long accessTokenExpired;
}
