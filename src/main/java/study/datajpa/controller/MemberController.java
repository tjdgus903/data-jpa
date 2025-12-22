package study.datajpa.controller;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import study.datajpa.dto.MemberDto;
import study.datajpa.entity.Member;
import study.datajpa.repository.MemberRepository;

@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberRepository memberRepository;

    @GetMapping("/members/{id}")
    public String findMember(@PathVariable("id") Long id){
        Member member = memberRepository.findById(id).get();
        return member.getUsername();
    }

    // 도메인 클래스 컨버터로 엔티티를 파라미터로 받으면,
    // 이 엔티티는 단순 조회용으로만 사용해야됨
    @GetMapping("/members2/{id}")
    public String findMember2(@PathVariable("id") Member member){
        return member.getUsername();
    }

    // http://localhost:8080/members?page=0&size=3&sort=id,desc&sort=username,desc
    // 컨트롤러가 pageable 이 있으면 pageRequset 를 반환하여 함수를 사용할 수 있게끔 해줌
    // 기본 디폴트값은 20개로 잡혀있고 기본값을 변경하고싶으면 yaml 파일을 변경해서 적용 가능
    // 매개변수로 파라미터값을 5로 설정하면 이 값이 우선권을 가짐
    // 또한 반환타입을 api를 get 처럼 url로 노출해버리는건 매우 위험함
    @GetMapping("/members")
    public Page<MemberDto> list(@PageableDefault(size=5) Pageable pageable){
        // Page<Member> page = memberRepository.findAll(pageable);
        // 기존 방식인 page 를 그대로 return 할 경우 해당 정보에 대한 모든 데이터들이 조회되어 전달됨(json 으로 조회 가능)
        // 하지만 map 을 사용하면 해당 데이터 중 필요한 값만 조회하여 전달하기 때문에 안전함
        // Page<MemberDto> map = page.map(member -> new MemberDto(member.getId(), member.getUsername(), null));
        return memberRepository.findAll(pageable)
                .map(MemberDto::new);
    }

    @PostConstruct
    public void init(){
        for (int i=0; i<100; i++) {
            memberRepository.save(new Member("user" + i, i));
        }
    }
}
