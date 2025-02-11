package com.luckyparty.auth.service;

import com.luckyparty.auth.controller.request.LoginRequest;
import com.luckyparty.auth.controller.request.RefreshRequest;
import com.luckyparty.auth.controller.request.RegisterRequest;
import com.luckyparty.auth.controller.response.UserResponse;
import com.luckyparty.common.jwt.response.JwtResponse;

public interface AuthService {

    public UserResponse register(RegisterRequest registerRequest);

    public JwtResponse login(LoginRequest loginRequest);

    public JwtResponse refresh(RefreshRequest refreshRequest);
}
