package com.bingbong.book.springboot.web;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@RunWith(SpringRunner.class) // -> 테스트를 진행할 때 JUnit에 내장된 실행자가 아닌 다른 실행자를 실행시킨다. 스프링부트 테스트와 JUnit 사이에 연결자 역할
@SpringBootTest(webEnvironment = RANDOM_PORT)
public class IndexControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void loadingMain(){
        String body = this.restTemplate.getForObject("/", String.class);

        assertThat(body).contains("스프링 부트로 시작하는 웹 서비스");
    }

}
