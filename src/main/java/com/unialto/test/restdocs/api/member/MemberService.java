package com.unialto.test.restdocs.api.member;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
@Slf4j
public class MemberService {
    public Member getMember(long idx) {
        // TODO Database
        return Member.builder()
                .idx(idx)
                .email("unialto@nate.com")
                .name("대끼")
                .birthdate("1983-11-22")
                .gender("남")
                .build();
    }

    public List<Member> getMembers() {
        // TODO Database
        return Arrays.asList(
                Member.builder()
                        .idx(1).email("unialto@nate.com").name("대끼")
                        .birthdate("1983-11-22").gender("남")
                        .build(),
                Member.builder()
                        .idx(2).email("test@gmail.com").name("테스트")
                        .birthdate("1990-01-22").gender("여")
                        .build(),
                Member.builder()
                        .idx(3).email("tester@gmail.com").name("테스터")
                        .birthdate("2010-11-30").gender("여")
                        .build()
        );
    }

    public void saveMember(Member member) {
        // TODO Database
        log.info("join : " + member.toString());
    }

    public void deleteMember(long idx) {
        // TODO Database
        log.info("delete : " + idx);
    }
}
