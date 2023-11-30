package com.example.bookrent.domain.user.service;


import com.example.bookrent.application.ui.request.SignUpRequest;
import com.example.bookrent.domain.user.model.User;
import com.example.bookrent.domain.user.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    public final UserRepository userRepository;


    public boolean checkDuplicatedEmail(String email) {

        return userRepository.findByEmail(email).isPresent();

    }


    public void createUser(SignUpRequest signUpRequest) {

        User user = User.create(signUpRequest);

        userRepository.save(user);


    }


    public void updateUserStatus(String email) {

        User user = userRepository.findByEmail(email).orElseThrow(
                () -> new EntityNotFoundException("해당 유저를 찾을수 없습니다" + email)
        );
        user.getVerificationForSignUp();

    }

}
