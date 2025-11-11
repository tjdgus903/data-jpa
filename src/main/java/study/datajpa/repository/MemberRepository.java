package study.datajpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import study.datajpa.dto.MemberDto;
import study.datajpa.entity.Member;

import java.util.List;

// JpaRepository 안에 많은 함수들을 포함하고 있기 때문에
// Repository 선언 시 extends로 가져와야됨
// interface 끼리 상속받을 때는 extends 사용

// Spring Data JPA 는 개발자가 정의한 repository interface 를 인식하고,
// 런타임에 해당 인터페이스의 실제 동작을 수행하는 proxy 객체를 생성하여 Spring bean으로 등록
// 이 덕분에 개발자는 인터페이스를 정의함으로써 repository 기능을 사용가능

// 엔티티의 필드명이 변경되면 인터페이스에 정의한 메서드 이름도 변경해야됨
// 그렇지 않으면 애플리케이션 시작 시점에서 오류가 발생함
public interface MemberRepository extends JpaRepository<Member, Long> {

    // Spring JPA Data 에서 메소드 이름을 분석하여 JPQL을 생성하고 실행함
    List<Member> findByUsernameAndAgeGreaterThan(String username, int age);

    // 파라미터를 안넣으면 전체 조회
    Member findTop3HelloBy();

    // Query 에 :username  이런식으로 파라미터값이 있으면 매개변수로 @Param 을 붙혀서 사용해야됨
    // JPQL 에서 repository 를 선언할 때 상단에 Query 를 붙혀서 사용하며 실무에서 많이 씀(복잡한 쿼리를 사용할 때)
    // 애플리케이션 로딩 시점에서 Query들을 모두 파싱해버림
    // 파싱하는 시점에서 문법 오류가 발생하면 그런 오류들도 모두 잡아버림
    @Query("select m from Member m where m.username = :username and m.age = :age")
    List<Member> findUser(@Param("username") String username, @Param("age") int age);

    @Query("select m.username from Member m")
    List<String> findUsernameList();

    // DTO 를 선언해서 JPQL 사용하는 방법
    // 선언하여 사용할 DTO 를 Query 에서 new study.datajpa.dto.MemberDto 이런 식으로 사용하면 됨
    @Query("select new study.datajpa.dto.MemberDto(m.id, m.username, t.name) from Member m join m.team t")
    List<MemberDto> findMemberDto();
}
