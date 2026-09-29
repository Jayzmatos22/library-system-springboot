package com.springBootStudy.study.service.user;

import com.springBootStudy.study.model.UserDetailsAux;
import com.springBootStudy.study.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;
    private static final String USER_NOTfOUND = "Credenciais de acesso inválidas";

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException{
        return userRepository.findByEmail(email)
                .map(UserDetailsAux::new)
                .orElseThrow(() -> new UsernameNotFoundException(USER_NOTfOUND));
    }


}
