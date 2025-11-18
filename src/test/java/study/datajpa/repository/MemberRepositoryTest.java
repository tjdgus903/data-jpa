package study.datajpa.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jdk.swing.interop.SwingInterOpUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import study.datajpa.dto.MemberDto;
import study.datajpa.entity.Member;
import study.datajpa.entity.Team;

import javax.swing.text.html.Option;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
@Rollback(false)
class MemberRepositoryTest {

    // spring jpa 가 구현클래스를 만들어서 객체에 꽂아버림
    // 구현체를 개발자가 아닌 spring jpa 가 만들어서 여기에 injection을 해버림
    @Autowired MemberRepository memberRepository;
    @Autowired TeamRepository teamRepository;
    @PersistenceContext
    EntityManager em;

    @Test
    public void testMember(){
        System.out.println("memberRepository = " + memberRepository.getClass());
        Member member = new Member("memberA");
        // interface 만 만들어두면 나머지는 springjpa가 다 넣어줌(save, findById . . .)
        Member savedMember = memberRepository.save(member);

        Member findMember = memberRepository.findById(savedMember.getId()).get();

        assertThat(findMember.getId()).isEqualTo(member.getId());
        assertThat(findMember.getUsername()).isEqualTo(member.getUsername());
        assertThat(findMember).isEqualTo(member);
    }

    @Test
    public void basicCRUD(){
        Member member1 = new Member("member1");
        Member member2 = new Member("member2");
        memberRepository.save(member1);
        memberRepository.save(member2);

        // 단건 조회 검증
        Member findMember1 = memberRepository.findById(member1.getId()).get();
        Member findMember2 = memberRepository.findById(member2.getId()).get();
        assertThat(findMember1).isEqualTo(member1);
        assertThat(findMember2).isEqualTo(member2);

        // 리스트 조회 검증
        List<Member> all = memberRepository.findAll();
        assertThat(all.size()).isEqualTo(2);

        // 카운트 검증
        long count = memberRepository.count();
        assertThat(count).isEqualTo(2);

        // 삭제 검증
        memberRepository.delete(member1);
        memberRepository.delete(member2);

        long deleteCount = memberRepository.count();
        assertThat(deleteCount).isEqualTo(0);
    }

    @Test
    public void findByUsernameAndAgeGreaterThen(){
        Member m1 = new Member("AAA", 10);
        Member m2 = new Member("AAA", 20);
        memberRepository.save(m1);
        memberRepository.save(m2);

        List<Member> result = memberRepository.findByUsernameAndAgeGreaterThan("AAA", 15);

        assertThat(result.get(0).getUsername()).isEqualTo("AAA");
        assertThat(result.get(0).getAge()).isEqualTo(20);
        assertThat(result.size()).isEqualTo(1);
    }

    @Test
    public void findHelloBy(){
        Member helloBy = memberRepository.findTop3HelloBy();
    }

    @Test
    public void findUserNameList(){
        Member m1 = new Member("AAA",10);
        Member m2 = new Member("BBB",20);
        memberRepository.save(m1);
        memberRepository.save(m2);

        List<String> usernameList = memberRepository.findUsernameList();
        for (String s : usernameList) {
            System.out.println("s = "+s);
        }
    }

    @Test
    public void findMemberDto(){
        Team team = new Team("teamA");
        teamRepository.save(team);

        Member m1 = new Member("AAA",10);
        m1.setTeam(team);
        memberRepository.save(m1);

        List<MemberDto> usernameList = memberRepository.findMemberDto();
        for (MemberDto dto : usernameList) {
            System.out.println("dto = "+dto);
        }
    }

    @Test
    public void findByNames(){
        Member m1 = new Member("AAA",10);
        Member m2 = new Member("BBB",20);
        memberRepository.save(m1);
        memberRepository.save(m2);

        List<Member> result = memberRepository.findByNames(Arrays.asList("AAA","BBB"));
        for (Member member : result) {
            System.out.println("member = "+member);
        }
    }

    @Test
    public void returnType(){
        Member m1 = new Member("AAA", 10);
        Member m2 = new Member("AAA", 20);
        memberRepository.save(m1);
        memberRepository.save(m2);

        List<Member> aaa = memberRepository.findListByUsername("AAA");
        Member bbb = memberRepository.findMemberByUsername("BBB");
        System.out.println("bbb : "+bbb);
        Optional<Member> ccc = memberRepository.findOptionalByUsername("AAA");
        System.out.println("ccc : "+ccc);

        // jpa 에서 List 의 경우 값이 맞지 않더라도 null 을 내보내지 않음(값이 없으면 0)
        List<Member> ddd = memberRepository.findListByUsername("asdf");
        System.out.println("ddd : "+ddd.size());

        Member eee = memberRepository.findMemberByUsername("asdf");
        System.out.println("eee : "+eee);

        Optional<Member> fff = memberRepository.findOptionalByUsername("asdf");
        System.out.println("fff : "+fff);
    }

    @Test
    public void paging(){
        // given
        memberRepository.save(new Member("member1", 10));
        memberRepository.save(new Member("member2", 10));
        memberRepository.save(new Member("member3", 10));
        memberRepository.save(new Member("member4", 10));
        memberRepository.save(new Member("member5", 10));

        int age = 10;
        // 0 페이지에서 3개 가져와, Sort 정렬은 DESC/ASC 차순으로 "username" 기준 정렬
        PageRequest pageRequest = PageRequest.of(0, 3, Sort.by(Sort.Direction.ASC, "username"));

        // when
        // 이거는 Controller 에서 바로 노출시키면 큰일남..
        // Entity를 외부에 바로 노출시키면 안되기 때문에
        // Application 안에 숨기고 dto로 반환시켜서 넘겨야됨
        Page<Member> page = memberRepository.findByAge(age, pageRequest);

        // map 이란 내부의 데이터를 다른 값으로 반환해서 뽑는 함수
        // 외부에 데이터를 뽑을때는 이렇게 dto로 반환해서 사용해서 뽑아야됨(이건 나중에 다시 보기)
        Page<MemberDto> toMap = page.map(m -> new MemberDto(m.getId(), m.getUsername(), null));

        // then
        List<Member> content = page.getContent();
        long totalElements = page.getTotalElements();
        for (Member member : content) {
            System.out.println("member : "+member);
        }
        System.out.println("total : "+totalElements);

        assertThat(content.size()).isEqualTo(3);
        assertThat(page.getTotalElements()).isEqualTo(5);
        assertThat(page.getNumber()).isEqualTo(0);
        assertThat(page.getTotalPages()).isEqualTo(2);
        assertThat(page.isFirst()).isTrue();
        assertThat(page.hasNext()).isTrue();
    }

    /*@Test
    public void slice(){
        // given
        memberRepository.save(new Member("member1", 10));
        memberRepository.save(new Member("member2", 10));
        memberRepository.save(new Member("member3", 10));
        memberRepository.save(new Member("member4", 10));
        memberRepository.save(new Member("member5", 10));

        int age = 10;
        // 0 페이지에서 3개 가져와, Sort 정렬은 DESC/ASC 차순으로 "username" 기준 정렬
        PageRequest pageRequest = PageRequest.of(0, 3, Sort.by(Sort.Direction.ASC, "username"));

        // when
        Slice<Member> page = memberRepository.findByAge(age, pageRequest);

        // then
        List<Member> content = page.getContent();
        //long totalElements = page.getTotalElements();
        for (Member member : content) {
            System.out.println("member : "+member);
        }
        //System.out.println("total : "+totalElements);

        assertThat(content.size()).isEqualTo(3);
        //assertThat(page.getTotalElements()).isEqualTo(5);
        assertThat(page.getNumber()).isEqualTo(0);
        //assertThat(page.getTotalPages()).isEqualTo(2);
        assertThat(page.isFirst()).isTrue();
        assertThat(page.hasNext()).isTrue();
    }*/

    @Test
    public void bulkUpdate(){
        // given
        memberRepository.save(new Member("member1",10));
        memberRepository.save(new Member("member2",13));
        memberRepository.save(new Member("member3",19));
        memberRepository.save(new Member("member4",12));
        memberRepository.save(new Member("member5",31));
        memberRepository.save(new Member("member6",22));
        memberRepository.save(new Member("member7",40));

        // when
        // bulk update 는 바로 db에 업데이트를 해버리기 때문에
        // 영속성컨텍스트에 남아있는 데이터와 수치가 다를 수 있음
        // 그렇기 때문에 bulk 연산을 한 뒤에는 영속성 컨텍스트를 초기화 해야됨
        int resultCount = memberRepository.bulkAgePlus(20);
        //em.flush();
        //em.clear();

        List<Member> result = memberRepository.findListByUsername("member7");
        Member member7 = result.get(0);
        System.out.println("member7 : "+member7);

        // then
        assertThat(resultCount).isEqualTo(3);
    }

    @Test
    public void findMemberLazy(){
        // given
        //member1 -> teamA
        //member2 -> teamB

        Team teamA = new Team("teamA");
        Team teamB = new Team("teamB");
        teamRepository.save(teamA);
        teamRepository.save(teamB);
        Member member1 = new Member("member1", 10, teamA);
        Member member2 = new Member("member2", 10, teamB);
        memberRepository.save(member1);
        memberRepository.save(member2);

        em.flush();
        em.clear();

        // when
        // N + 1 문제 - function을 수행 시 연관되어있는 객체들을 불러들이기 위해 추가적인 조회를 하는 것
        // (네트워크를 타기 때문에 N이 많아질수록 성능 이슈가 생김)
        // 1. Member 만 조회
        // List<Member> members = memberRepository.findAll();
        // fetch join 은 연관되어있는 데이터를 join 을 통해 다 긁어오는 것
        // join 해서 데이터를 한번에 가져오기 때문에 네트워크를 여러번 탈 필요없이 가져옴
        // List<Member> members = memberRepository.findMemberFetchJoin();
        // EntityGraph 는 fetch join
        // List<Member> members = memberRepository.findAll();
        // EntityGraph 는 파라미터를 통해 where 문 조건을 추가할 수 있음
        List<Member> members = memberRepository.findEntityGraphByUsername("member1");

        for (Member member : members) {
            // 2. 최초 member 를 조회할 때 member의 Team 은 가짜 객체만 가지고 옴
            // LAZY로 ManyToOne 으로 연결되어있으면 Team 데이터를 조회하기 전에는 가짜 Proxy 객체를 불러드림
            System.out.println("member = "+member.getUsername());
            // 3. 해당 데이터와 연관된 데이터(member 에서 Team 객체)가 애플리케이션 로딩이 안되어있기 때문에
            // 조회 시에는 select 쿼리를 통해 해당 데이터를 조회하여 가지고 옴
            System.out.println("member.teamClass = "+member.getTeam().getClass());
            // 4. 그리고 조회된 데이터를 뿌려줌
            System.out.println("member.team = "+member.getTeam().getName());
        }
    }
}