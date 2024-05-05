package SogangSolutionShare.BE.Intercepter;

import SogangSolutionShare.BE.domain.Member;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;

@Slf4j
public class LoginCheckInterceptor implements HandlerInterceptor {

    // 로그인 체크 인터셉터
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        log.info("로그인 체크 인터셉터 실행");

        HttpSession session = request.getSession();
        Member member = (Member) session.getAttribute("loginMember");

        log.info("로그인 사용자 정보: {}", member);

        if (member == null) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "로그인이 필요합니다.");
            return false;
        }

        log.info("로그인 사용자 인증 체크 완료");

        return HandlerInterceptor.super.preHandle(request, response, handler);
    }
}
