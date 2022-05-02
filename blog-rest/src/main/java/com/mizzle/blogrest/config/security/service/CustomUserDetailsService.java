package com.mizzle.blogrest.config.security.service;

import javax.transaction.Transactional;

import com.mizzle.blogrest.config.security.token.UserPrincipal;
import com.mizzle.blogrest.entity.user.User;
import com.mizzle.blogrest.error.BadRequestException;
import com.mizzle.blogrest.repository.user.UserRepository;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class CustomUserDetailsService implements UserDetailsService{

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        
        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new UsernameNotFoundException("User not found with email : " + email)
        );

        return UserPrincipal.create(user);
    }

    @Transactional
    public UserDetails loadUserById(Long id) {
        User user = userRepository.findById(id).orElseThrow(
            () -> new BadRequestException(""+id)
        );

        return UserPrincipal.create(user);
    }
    
}
