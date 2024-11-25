package com.ssafy.urs.controller;

import com.ssafy.urs.dto.LoginRequest;
import com.ssafy.urs.entity.RegisterRequest;
import com.ssafy.urs.entity.User;
import com.ssafy.urs.repository.UserRepository;
import com.ssafy.urs.service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
public class AuthController {

    private final UserService userService;
    private final UserRepository userRepository;

    /**
     * 회원가입
     */
    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterRequest request) {
        try {
            User user = new User(request.getUserId(), request.getPassword(), request.getName(), request.getPhone());
            userService.registerUser(user);
            return ResponseEntity.ok("User registered successfully");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/register/check-id")
    public ResponseEntity<Boolean> checkDuplicateId(@RequestParam String userId) {
        try {
            userService.findUserById(userId);
            return ResponseEntity.ok(true);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.OK).body(false);
        }
    }

    /**
     * 로그인
     */
    @PostMapping("/login")
    public ResponseEntity<String> login(
            @RequestBody LoginRequest loginRequest,
            HttpSession session,
            HttpServletResponse response) {

        String userId = loginRequest.getUserId();
        String password = loginRequest.getPassword();

        // 사용자 검증 로직
        Optional<User> userOptional = userRepository.findByUserId(userId);
        if (userOptional.isPresent() && password.equals(userOptional.get().getPassword())) {
            User user = userOptional.get();

            // 세션에 사용자 정보 저장
            session.setAttribute("userId", user.getUserId());
            session.setAttribute("userName", user.getName());
            System.out.println("session userId = " + session.getAttribute("userId"));
            System.out.println("session userName = " + session.getAttribute("userName"));

            // 세션 ID 쿠키 생성
            Cookie sessionCookie = new Cookie("SESSIONID", session.getId());
            sessionCookie.setHttpOnly(true);
            sessionCookie.setPath("/");
            sessionCookie.setMaxAge(60 * 30); // 30분
            response.addCookie(sessionCookie);

            return ResponseEntity.ok("Login successful");
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpSession session) {
        session.invalidate(); // 세션 무효화
        return ResponseEntity.ok("Logout successful");
    }

    private boolean isValidUser(String userId, String password) {
        Optional<User> user = userRepository.findByUserId(userId);
        if (user.isPresent()) {
            // 비밀번호 검증
            return password.equals(user.get().getPassword());
        }
        return false;
    }
}
