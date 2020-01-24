package com.bingbong.book.springboot.config.auth;

import com.bingbong.book.springboot.domain.user.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@RequiredArgsConstructor
@EnableWebSecurity // Spring security 활성화
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final CustomOAuth2UserService customOAuth2UserService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .headers().frameOptions().disable() //h2-console 화면을 사용하기 위해 disable
                .and()
                    .authorizeRequests() // URL 별 권한 관리 설정 옵션의 시작점
                    .antMatchers("/", "/css/**", "/images/**", "/js/**", "/h2-console/**").permitAll() // 전체 열람 권한을 줌
                    .antMatchers("/api/v1/**").hasRole(Role.USER.name()) // USER 권한이 있는 사람만 해당 URL이 가능
                    .anyRequest().authenticated() // 나머지 URL은 인증된 사용자(로그인한 사용자)만 허용
                .and()
                    .logout() // 로그아웃 성공시 "/"로
                        .logoutSuccessUrl("/")
                .and()
                    .oauth2Login()
                        .userInfoEndpoint() //로그인 성공 이후 사용자 정보를 가져올 때
                            .userService(customOAuth2UserService);
    }
}
