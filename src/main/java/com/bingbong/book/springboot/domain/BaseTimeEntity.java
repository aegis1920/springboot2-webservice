package com.bingbong.book.springboot.domain;

import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@MappedSuperclass // -> JPA Entity 클래스들이 BaseTimeEntity를 상속할 경우 여기 있는 필드들도 칼럼으로 인식하도록 해줌
@EntityListeners(AuditingEntityListener.class) // -> JPA Auditing을 이용해 등록/수정 시간을 자동화시켜줄 수 있다.
public class BaseTimeEntity {

    @CreatedDate // -> Entity가 생성되어 저장될 때 자동으로 저장
    private LocalDateTime createdDate;

    @LastModifiedDate
    private LocalDateTime modifiedDate;

}
