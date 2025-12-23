package study.datajpa.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;

@Entity
@Getter
public class Item {

    // GeneratedValue 는 jpa 에 등록(persist)을 하면 그 안에서 값이 들어감
    // 그 전까지 id 값이 안생김
    @Id
    @GeneratedValue
    private Long id;
}
