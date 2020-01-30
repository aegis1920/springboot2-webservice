package com.bingbong.book.springboot.domain.posts;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PostsRepository extends JpaRepository<Posts, Long> {
    @Query("SELECT p FROM Posts p ORDER BY  p.id DESC") // -> Spring Data JPA가 제공하는 쿼리 메소드 기능. 기본으로 JPQL을 사용하고 가독성이 좋지만 JPA의 장점을 활용할 수 없다.
    List<Posts> findAllDesc();
}
