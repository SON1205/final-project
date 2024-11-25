package com.ssafy.urs.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class SessionFilter extends HttpFilter {

    // 허용 경로 목록
    private static final List<String> ALLOWED_PATHS = List.of(
            "/api/login",
            "/api/register",
            "/api/secure/check"
    );

    @Override
    protected void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        String requestURI = request.getRequestURI();

        // 허용된 경로인 경우 요청을 그대로 진행
        if (isAllowedPath(requestURI)) {
            chain.doFilter(request, response);
            return;
        }

        // 세션 확인
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Unauthorized access");
            return;
        }

        // 인증된 세션이 있는 경우 요청 진행
        chain.doFilter(request, response);
    }

    // 요청 경로가 허용된 경로인지 확인
    private boolean isAllowedPath(String requestURI) {
        return ALLOWED_PATHS.stream().anyMatch(requestURI::startsWith);
    }
}
