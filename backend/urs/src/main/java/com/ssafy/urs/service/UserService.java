package com.ssafy.urs.service;

import com.ssafy.urs.entity.User;

public interface UserService {
    void registerUser(User user);            // 사용자 등록

    User findUserById(String userId);        // 사용자 조회
}

