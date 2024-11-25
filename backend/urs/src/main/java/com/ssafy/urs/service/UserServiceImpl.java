package com.ssafy.urs.service;

import com.ssafy.urs.entity.User;
import com.ssafy.urs.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    /**
     * 사용자 등록 (회원가입)
     */
    @Override
    public void registerUser(User user) {
        if (userRepository.findByUserId(user.getUserId()).isPresent()) {
            throw new IllegalArgumentException("User ID already exists");
        }

        // 사용자 저장
        userRepository.save(user);
    }

    /**
     * 사용자 조회
     */
    @Override
    public User findUserById(String userId) {
        return userRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + userId));
    }
}
