package study.data_jpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import study.data_jpa.entity.Member;

// JpaRepository 안에 많은 함수들을 포함하고 있기 때문에
// Repository 선언 시 extends로 가져와야됨
// interface 끼리 상속받을 때는 extends 사용
public interface MemberRepository extends JpaRepository<Member, Long> {

}
