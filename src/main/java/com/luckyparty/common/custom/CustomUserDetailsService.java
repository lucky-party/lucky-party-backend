package com.luckyparty.common.custom;

import com.luckyparty.auth.persistence.entity.UserEntity;
import com.luckyparty.auth.persistence.repository.UserEntityRepository;
import com.luckyparty.common.exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class CustomUserDetailsService implements UserDetailsService {

    private final UserEntityRepository repository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("username : {}", username);

        UserEntity user = repository.findByEmail(username)
                .orElseThrow(UserNotFoundException::new);

        return new CustomUserDetails(user);
    }
}
