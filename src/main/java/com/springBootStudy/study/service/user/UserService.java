package com.springBootStudy.study.service.user;

import com.springBootStudy.study.dtos.user.RegisterResponseDTO;
import com.springBootStudy.study.dtos.user.RegisterUserDTO;
import com.springBootStudy.study.exceptions.api.EmailAlreadyExistsException;
import com.springBootStudy.study.model.Book;
import com.springBootStudy.study.model.User;
import com.springBootStudy.study.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public User createUser(RegisterUserDTO userDto){
        Optional<User> existUser = userRepository.findByEmail(userDto.email());

        if (existUser.isPresent()){
            throw new EmailAlreadyExistsException(userDto.email());
        }

        User user = new User();
        user.setName(userDto.name());
        user.setEmail(userDto.email());
        user.setPassword(passwordEncoder.encode(userDto.password()));

        userRepository.save(user);

    }
}
