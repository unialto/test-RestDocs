package com.unialto.test.restdocs.api.member;

import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Member {
    private long idx;
    private String email;
    private String name;
    private String birthdate;
    private String gender;
}
