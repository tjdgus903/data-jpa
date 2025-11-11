package study.datajpa.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(of = {"id","name"})
public class Team {

    @Id @GeneratedValue
    @Column(name = "team_id")
    private Long id;
    private String name;

    // 팀은 하나이고 member는 N이기 때문에 1대 다(항상 선언주체가 앞으로) - OneToMany
    // 외래키가 없는 쪽에 mappedBy를 지정해야됨
    // 현재 member쪽에서 team 을 참조하기 때문에 외래키는 member
    @OneToMany(mappedBy = "team")
    private List<Member> members=new ArrayList<>();

    public Team(String name) {
        this.name = name;
    }
}
