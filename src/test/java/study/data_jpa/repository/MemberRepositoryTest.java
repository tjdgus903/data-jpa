package study.data_jpa.repository;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import study.data_jpa.entity.Member;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@Rollback(false)
class MemberRepositoryTest {

    // spring jpa 가 구현클래스를 만들어서 객체에 꽂아버림
    // 구현체를 개발자가 아닌 spring jpa 가 만들어서 여기에 injection을 해버림
    @Autowired MemberRepository memberRepository;

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
}