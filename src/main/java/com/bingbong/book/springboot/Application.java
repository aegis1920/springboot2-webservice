package com.bingbong.book.springboot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

// @EnableJpaAuditing 여기다 해주게 되면 HelloControllerTest에서 엔티티가 필요하다고 나오게 된다. @WebMvcTest에서는 엔티티 클래스는 당연히 없는데. SpringBootApplication과 함께 있다보니 @WebMvcTest에서도 스캔해 발생한 현상이라 JpaConfig에 따로 붙여준다.
@SpringBootApplication
public class Application {
    public static void main(String[] args){
        SpringApplication.run(Application.class, args);
    }
}
