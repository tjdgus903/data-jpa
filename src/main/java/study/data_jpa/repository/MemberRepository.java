package study.data_jpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import study.data_jpa.entity.Member;

import java.util.List;

// JpaRepository 안에 많은 함수들을 포함하고 있기 때문에
// Repository 선언 시 extends로 가져와야됨
// interface 끼리 상속받을 때는 extends 사용

// Spring Data JPA 는 개발자가 정의한 repository interface 를 인식하고,
// 런타임에 해당 인터페이스의 실제 동작을 수행하는 proxy 객체를 생성하여 Spring bean으로 등록
// 이 덕분에 개발자는 인터페이스를 정의함으로써 repository 기능을 사용가능
public interface MemberRepository extends JpaRepository<Member, Long> {

    // 이렇게 커스텀을 하면 필요한거에 맞게끔 추가해야되는데 이건 나중에 알려준다고 함..
    List<Member> findByUsername(String username);

}
