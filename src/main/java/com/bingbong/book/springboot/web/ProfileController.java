package com.bingbong.book.springboot.web;

import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RequiredArgsConstructor
@RestController
public class ProfileController {
    private final Environment env;

    @GetMapping("/profile")
    public String profile(){
        // 현재 실행 중인 ActiveProfile을 모두 가져온다. real, oauth, real-db가 활성화되어있다면 3개 모두 담겨있다.
        List<String> profiles = Arrays.asList(env.getActiveProfiles());
        List<String> realProfiles = Arrays.asList("real", "real1", "real2");

        String defaultProfile = profiles.isEmpty() ? "default" : profiles.get(0);

        // real, real1, real2 중 하나라도 있다면 그 값을 반환한다.
        return profiles.stream()
                .filter(realProfiles::contains)
                .findAny()
                .orElse(defaultProfile);
    }

}
