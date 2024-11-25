package com.ssafy.urs.validator;

public class UserValidator {

    /**
     * 입력 데이터 유효성 검사
     */
    public static void validateInput(String userId, String password, String name, String phone) {
        if (userId == null || userId.trim().isEmpty()) {
            throw new IllegalArgumentException("User ID cannot be null or empty");
        }

        if (password == null || password.length() < 6) {
            throw new IllegalArgumentException("Password must be at least 6 characters long");
        }

        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Name cannot be null or empty");
        }

        if (!isValidPhone(phone)) {
            throw new IllegalArgumentException("Phone number must be between 10 and 15 digits");
        }
    }

    /**
     * 전화번호 유효성 검사
     */
    private static boolean isValidPhone(String phone) {
        return phone != null && phone.matches("^[0-9]{10,15}$");
    }
}

