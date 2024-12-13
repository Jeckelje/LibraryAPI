package com.modsen.bookstorageservice.security;

import com.modsen.bookstorageservice.mapper.UserMapper;
import com.modsen.bookstorageservice.model.User;
import com.modsen.bookstorageservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JwtUserDetailsService implements UserDetailsService {

    private final UserService userService;
    private final UserMapper userMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userMapper.toUser(userService.getUserByUsername(username));
        return JwtEntityFactory.createJwtEntity(user);
    }
}
