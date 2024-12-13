package com.modsen.booktrackerservice.service;

import com.modsen.booktrackerservice.dto.request.CreateJwtRequest;
import com.modsen.booktrackerservice.dto.response.JwtResponse;
import org.springframework.stereotype.Service;

@Service
public interface AuthService {

    JwtResponse login (CreateJwtRequest loginRequest);

    JwtResponse refresh(String refreshToken);

}

