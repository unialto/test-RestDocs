package com.unialto.test.restdocs.api.member;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/member")
@RequiredArgsConstructor
public class MemberController {
    private final MemberService service;

    @GetMapping("{idx}")
    public ResponseEntity<?> member(@PathVariable("idx") long idx) {
        Member member = service.getMember(idx);

        return new ResponseEntity<>(member, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<?> members(@RequestParam(value = "next", required = false, defaultValue = "0") long nextIdx,
                                     @RequestParam(value = "limit", required = false, defaultValue = "10") int limit) {
        List<Member> members = service.getMembers();

        return new ResponseEntity<>(members, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<?> join(@RequestBody Member member) {
        if (member.getEmail() == null) {
            throw new NullPointerException("member.email");
        }

        log.debug("member : " + member.toString());

        service.saveMember(member);

        return new ResponseEntity<>(Map.of("result", "succ"), HttpStatus.OK);
    }

    @DeleteMapping
    public ResponseEntity<?> delete(@RequestParam("idx") long idx) {
        if (idx == 0) {
            throw new NullPointerException("idx");
        }

        log.debug("idx : " + idx);

        service.deleteMember(idx);

        return new ResponseEntity<>(Map.of("result", "succ"), HttpStatus.OK);
    }
}
