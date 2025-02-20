package org.zerock.b02.domain;

import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@MappedSuperclass   //  entity 를 상속해주기 위한 어노테이션
@EntityListeners(value = {AuditingEntityListener.class})    // Auditing 된 거에 대해서만 listener 로 설정
@Getter
public class BaseEntity {
    @CreatedDate    // insert할 경우에 기본적으로 생성시간을 주입
    @Column(name = "regdate", updatable = false)    // 실제 테이블의 컬럼 이름을 regDate이고 update 문에는 컬럼이 들어가지 않도록
    private LocalDateTime regDate;

    @LastModifiedDate   // update 할 경우에 마지막으로 update 하는 시간으로 세팅
    @Column(name = "moddate")
    private LocalDateTime modDate;
}
