package com.luckyparty.auth.controller;

import com.luckyparty.auth.controller.request.LoginRequest;
import com.luckyparty.auth.controller.request.RefreshRequest;
import com.luckyparty.auth.controller.request.RegisterRequest;
import com.luckyparty.auth.controller.response.UserResponse;
import com.luckyparty.auth.service.AuthService;
import com.luckyparty.common.exception.response.ExceptionResponse;
import com.luckyparty.common.jwt.response.JwtResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Tag(name = "사용자 Auth Controller", description = "사용자 Auth API Controller")
public class AuthController {

    private final AuthService authService;

    @Operation(summary = "사용자 회원가입 API", description = "사용자 회원가입 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = UserResponse.class))
            }, description = "성공 시 반환"),
            @ApiResponse(responseCode = "409", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class))
            }, description = "가입된 닉네임 또는 이메일로 재가입 요청 시 반환")
    })
    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    description = "사용자 회원가입 요청 객체",
                    content = @Content(schema = @Schema(implementation = RegisterRequest.class))
            )
            @RequestBody RegisterRequest request
    ){
        return ResponseEntity.ok(authService.register(request));
    }

    @Operation(summary = "사용자 로그인 API", description = "사용자 로그인 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = JwtResponse.class))
            }, description = "성공 시 반환"),
            @ApiResponse(responseCode = "404", content = {
                    @Content(schema = @Schema(implementation = ExceptionResponse.class))
            }, description = "사용자를 찾지 못했을 경우 반환")
    })
    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    description = "사용자 로그인 요청 객체",
                    content = @Content(schema = @Schema(implementation = LoginRequest.class))
            )
            @RequestBody LoginRequest loginRequest
    ){
        return ResponseEntity.ok(authService.login(loginRequest));
    }

    @Operation(summary = "토큰 재발급 API", description = "refresh 토큰을 활용한 access 토큰 재발급 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = JwtResponse.class))
            }, description = "성공 시 반환"),
            @ApiResponse(responseCode = "403", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class))
            }, description = "만료된 토큰일 경우 반환"),
            @ApiResponse(responseCode = "404", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class))
            }, description = "토큰 또는 사용자를 찾을 수 없을 경우 반환")
    })
    @PostMapping("/refresh")
    public ResponseEntity<JwtResponse> refresh(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    description = "토큰 재발급 요청 객체",
                    content = @Content(schema = @Schema(implementation = RefreshRequest.class))
            )
            @RequestBody RefreshRequest refreshRequest
    ){
        return ResponseEntity.ok(authService.refresh(refreshRequest));
    }
}
