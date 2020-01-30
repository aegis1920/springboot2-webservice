package com.bingbong.book.springboot.domain.posts;

import com.bingbong.book.springboot.domain.BaseTimeEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity
public class Posts extends BaseTimeEntity {
    @Id // -> 해당 테이블의 PK
    @GeneratedValue(strategy = GenerationType.IDENTITY) // -> IDENTITY 옵션을 주어야 auto_increment가 된다.
    private Long id;

    @Column(length = 500, nullable = false) // -> 선언하지 않더라도 클래스의 필드는 모두 칼럼이 되긴 함. 사이즈나 타입을 바꿀 때 사용.
    private String title;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    private String author;

    @Builder // -> 해당 클래스의 빌더 패턴 클래스 생성. 빌터 패턴을 쓰면 가독성이 좋아짐
    public Posts(String title, String content, String author){
        this.title = title;
        this.content = content;
        this.author = author;
    }

    public void update(String title, String content){
        this.title = title;
        this. content = content;
    }

}
