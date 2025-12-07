package study.datajpa.entity;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
// 속성을 밑에 내려서 테이블에 같이 사용할 수 있는 클래스
@MappedSuperclass
public class JpaBaseEntity {

    // 별도의 update 가 일어나도 update 가 되지 않음
    @Column(updatable = false)
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;

    // 최초 등록 시
    @PrePersist
    public void prePersist(){
        LocalDateTime now = LocalDateTime.now();
        createdDate = now;
        updatedDate = now;
    }

    // 업데이트 시
    @PreUpdate
    public void preUpdate(){
        updatedDate = LocalDateTime.now();
    }
}
