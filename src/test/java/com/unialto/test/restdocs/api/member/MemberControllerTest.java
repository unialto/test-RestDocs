package com.unialto.test.restdocs.api.member;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.restdocs.snippet.Attributes.key;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith({RestDocumentationExtension.class, SpringExtension.class})
@WebMvcTest(MemberController.class)
class MemberControllerTest {
    @Autowired
    private WebApplicationContext context;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private MemberService service;

    private MockMvc mockMvc;

    @BeforeEach
    public void setUp(RestDocumentationContextProvider restDocumentation) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .addFilter(new CharacterEncodingFilter("UTF-8", true))
                .apply(documentationConfiguration(restDocumentation).uris().withScheme("https").withHost("unialto.com"))
                .build();
    }

    @Test
    void member() throws Exception {
        Member member = Member.builder()
                .idx(1)
                .email("hong@gildong.kr")
                .name("?????????")
                .birthdate("2012-05-07")
                .gender("???")
                .build();


        when(service.getMember(1))
                .thenReturn(member);

        this.mockMvc.perform(get("/member/{idx}", 1).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(document(
                        "member",
                        preprocessResponse(prettyPrint()),
                        pathParameters(parameterWithName("idx").description("????????????")),
                        responseFields(
                                fieldWithPath("idx").type(JsonFieldType.NUMBER).description("????????????"),
                                fieldWithPath("email").type(JsonFieldType.STRING).description("?????????"),
                                fieldWithPath("name").type(JsonFieldType.STRING).description("??????"),
                                fieldWithPath("birthdate").type(JsonFieldType.STRING).description("?????? (yyyy-MM-dd)"),
                                fieldWithPath("gender").type(JsonFieldType.STRING).description("?????? (???, ???)")
                        )
                ))
                .andDo(print());
    }

    @Test
    void members() throws Exception {
        List<Member> members = Arrays.asList(
                Member.builder()
                        .idx(1).email("hong@gildong.kr").name("?????????")
                        .birthdate("2012-05-07").gender("???")
                        .build(),
                Member.builder()
                        .idx(2).email("son@O0.com").name("?????????")
                        .birthdate("2011-05-10").gender("???")
                        .build(),
                Member.builder()
                        .idx(3).email("cho@sun.com").name("??????")
                        .birthdate("2013-05-10").gender("???")
                        .build()
        );

        when(service.getMembers())
                .thenReturn(members);

        this.mockMvc.perform(
                get("/member")
                        .param("next", "0")
                        .param("limit", "10")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(document(
                        "members",
                        preprocessResponse(prettyPrint()),
                        requestParameters(
                                parameterWithName("next").optional().attributes(key("default").value("0")).description("?????? ?????? ????????????"),
                                parameterWithName("limit").optional().attributes(key("default").value("10")).description("????????????")
                        ),
                        responseFields(
                                fieldWithPath("[].idx").type(JsonFieldType.NUMBER).description("????????????"),
                                fieldWithPath("[].email").type(JsonFieldType.STRING).description("?????????"),
                                fieldWithPath("[].name").type(JsonFieldType.STRING).description("??????"),
                                fieldWithPath("[].birthdate").type(JsonFieldType.STRING).description("?????? (yyyy-MM-dd)"),
                                fieldWithPath("[].gender").type(JsonFieldType.STRING).description("?????? (???, ???)")
                        )
                ))
                .andDo(print());
    }

    @Test
    void join() throws Exception {
        Member member = Member.builder()
                .email("hong@gildong.kr")
                .name("?????????")
                .birthdate("2012-05-07")
                .gender("???")
                .build();

        this.mockMvc.perform(
                post("/member")
                        .content(objectMapper.writeValueAsString(member))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(document(
                        "join",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("idx").ignored(),
                                fieldWithPath("email").type(JsonFieldType.STRING).description("?????????"),
                                fieldWithPath("name").type(JsonFieldType.STRING).description("??????"),
                                fieldWithPath("birthdate").type(JsonFieldType.STRING).optional().description("?????? (yyyy-MM-dd)"),
                                fieldWithPath("gender").type(JsonFieldType.STRING).optional().description("?????? (???, ???)")
                        ),
                        responseFields(
                                fieldWithPath("result").type(JsonFieldType.STRING).description("?????? (succ, fail)")
                        )
                ))
                .andDo(print());
    }

    @Test
    void delete() throws Exception {
        long idx = 1;

        this.mockMvc.perform(
                RestDocumentationRequestBuilders.delete("/member")
                        .param("idx", idx + "")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(document(
                        "delete",
                        preprocessResponse(prettyPrint()),
                        requestParameters(
                                parameterWithName("idx").description("????????????")
                        ),
                        responseFields(
                                fieldWithPath("result").type(JsonFieldType.STRING).description("?????? (succ, fail)")
                        )
                ))
                .andDo(print());
    }
}