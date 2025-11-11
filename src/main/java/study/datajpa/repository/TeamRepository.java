package study.datajpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import study.datajpa.entity.Team;


// JpaRepository 를 extends 해주면 @Repository 를 선언 안해줘도 프록시 객체로 인식함
// 프록시 객체란 대리인 역할을 하는 객체
public interface TeamRepository extends JpaRepository<Team, Long> {
}
