package com.bingbong.book.springboot;

import com.bingbong.book.springboot.config.auth.SecurityConfig;
import com.bingbong.book.springboot.web.HelloController;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
// @WebMvcTest는 컨트롤러를 포함한 몇 가지(`@Controller`는 스캔 가능 `@Service`, `@Component` 등은 스캔 불가)만 읽는다. SecurityConfig는 읽지만 SecurityConfig를 생성하기 위한 CustomOAuth2UserService는 읽을 수가 없다. @Service는 @WebMvcTest의 스캔 대상이 아니니까.
// 그리고 WebMvcTest는 JPA 기능이 작동하지 않는다. 이럴 때  JPA를 쓰려면 `@SpringBootTest`와 `TestRestTemplate 클래스`를 쓰면 된다.
// 이를 해결하기 위해 스캔 대상에서 SecurityConfig를 제거하는 것
@WebMvcTest(controllers = HelloController.class,
    excludeFilters = {
        @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = SecurityConfig.class)
    })
public class HelloControllerTest {

    @Autowired
    private MockMvc mvc; // 웹 API를 테스트할 때 사용(GET, POST 등..)

    @WithMockUser(roles = "USER")
    @Test
    public void helloReturn() throws Exception{
        String hello = "hello";

        mvc.perform(get("/hello"))
                .andExpect(status().isOk())
                .andExpect(content().string(hello));
    }

    @WithMockUser(roles = "USER")
    @Test
    public void helloDtoReturn() throws Exception{
        String name = "hello";
        int amount = 1000;

        // `perform` 메소드로 요청을 보낼 수 있고 200이면 어떤 데이터가 응답할 것인지 테스트해줄 수 있다.
        mvc.perform(
                get("/hello/dto")
                .param("name", name)
                .param("amount", String.valueOf(amount)) // `param` 메소드는 String만 가능하기에 숫자, 날짜 등의 데이터도 문자열로 변경해야된다.
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(name))) // `jsonPath`에서는 `$`를 기준으로 필드명을 명시한다.
                .andExpect(jsonPath("$.amount", is(amount)));
    }
}
