package study.data_jpa.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter @Setter
public class Member {

    @Id
    @GeneratedValue
    private Long id;
    private String username;

    // jpa Entity 는 기본적으로 생성자가 하나 있어야됨(파라미터 없이)
    protected Member(){
    }

    public Member(String username){
        this.username = username;
    }
}
