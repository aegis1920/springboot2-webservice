package com.bingbong.book.springboot.config.auth;

import com.bingbong.book.springboot.config.auth.dto.SessionUser;
import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpSession;


@RequiredArgsConstructor
@Component
// HandlerMethodArgumentResolver는 조건에 맞는 메소드가 있다면 구현체가 지정한 값으로 해당 메소드의 파리미터로 넘길 수 있다. @RequestBody나 @PathVariable 어노테이션을 사용할 때 사용한다.
public class LoginUserArgumentResolver implements HandlerMethodArgumentResolver {

    private final HttpSession httpSession;

    // 컨트롤러 메소드의 특정 파라미터를 지원하는지 판단한다.
    // 여기서는 @LoginUser 어노테이션이 붙어있고, SessionUser 클래스인 경우 true를 반환한다.
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        boolean isLoginUserAnnotation = parameter.getParameterAnnotation(LoginUser.class) != null;
        boolean isUserClass = SessionUser.class.equals(parameter.getParameterType());
        return isLoginUserAnnotation && isUserClass;
    }

    // 파라미터에 전달할 객체를 생성하는데 여기서는 세션에서 객체를 가져온다.
    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        return httpSession.getAttribute("user");
    }
}
