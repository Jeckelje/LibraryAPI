package com.modsen.bookstorageservice.service;

import com.modsen.bookstorageservice.dto.request.CreateJwtRequest;
import com.modsen.bookstorageservice.dto.response.JwtResponse;

public interface AuthService {

    JwtResponse login (CreateJwtRequest loginRequest);

    JwtResponse refresh(String refreshToken);

}

