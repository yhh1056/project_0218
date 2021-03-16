package com.github.homework.program.controller;

import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.put;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.github.homework.common.BaseControllerTest;
import com.github.homework.program.domain.Program;
import com.github.homework.program.model.ProgramSaveDto;
import com.github.homework.program.repository.ProgramRepository;
import com.github.homework.theme.domain.Theme;
import com.github.homework.theme.repository.ThemeRepository;

import java.util.Optional;
import java.util.Set;
import java.util.stream.IntStream;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;

public class ProgramControllerTest extends BaseControllerTest {
    @Autowired
    private ProgramRepository programRepository;

    @Autowired
    private ThemeRepository themeRepository;

    @Test
    @DisplayName("프로그램 단건 조회")
    public void getProgramTest() throws Exception {
        Program program = givenProgram(givenTheme("식도락여행"));
        this.mockMvc.perform(get("/api/programs/{id}", program.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").isNumber())
                .andExpect(jsonPath("name").value("여수 10미 먹거리"))
                .andExpect(jsonPath("introduction").value("여수시 일대 게장백반, 돌산갓김치등"))
                .andExpect(jsonPath("introductionDetail").value("여행자와 현지인이 꼽은 최고의 먹거리 여행지' 에서 대한민국 229개 지방자치단체 중 여수시가 1위에 선정되어 식도락 여행에 최적화된 프로그램"))
                .andExpect(jsonPath("region").value("전라남도 여수시"))
                .andDo(write.document(
                        pathParameters(
                                parameterWithName("id").description("id")
                        )
                ));
    }

    @Test
    @DisplayName("프로그램 단건 조회 결과 없음")
    public void getProgramFailTest() throws Exception {
        this.mockMvc.perform(get("/api/programs/{id}", 10L))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("프로그램 이름으로 조회")
    public void getProgramByName() throws Exception {
        Program program = givenProgram(givenTheme("식도락여행"));
        this.mockMvc.perform(get("/api/programs/name/{name}", program.getName()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").isNumber())
                .andExpect(jsonPath("name").value("여수 10미 먹거리"))
                .andExpect(jsonPath("introduction").value("여수시 일대 게장백반, 돌산갓김치등"))
                .andExpect(jsonPath("introductionDetail").value("여행자와 현지인이 꼽은 최고의 먹거리 여행지' 에서 대한민국 229개 지방자치단체 중 여수시가 1위에 선정되어 식도락 여행에 최적화된 프로그램"))
                .andExpect(jsonPath("region").value("전라남도 여수시"))
                .andDo(write.document(
                        pathParameters(
                                parameterWithName("name").description("program name")
                        )
                ));
    }

    @Test
    @DisplayName("프로그램 이름으로 조회 결과 없음")
    public void getProgramByNameNotFound() throws Exception {
        this.mockMvc.perform(get("/api/programs/name/{name}", "no"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("프로그램 page 조회")
    public void getPageProgramTest() throws Exception {
        givenProgram(givenTheme("식도락여행"));
        this.mockMvc.perform(get("/api/programs"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$..id").isNotEmpty())
                .andExpect(jsonPath("$..name").value("여수 10미 먹거리"))
                .andExpect(jsonPath("$..introduction").value("여수시 일대 게장백반, 돌산갓김치등"))
                .andExpect(jsonPath("$..introductionDetail").value("여행자와 현지인이 꼽은 최고의 먹거리 여행지' 에서 대한민국 229개 지방자치단체 중 여수시가 1위에 선정되어 식도락 여행에 최적화된 프로그램"))
                .andExpect(jsonPath("$..region").value("전라남도 여수시"))
                .andExpect(jsonPath("totalPages").value("1"))
                .andExpect(jsonPath("totalElements").value("1"));
    }

    @Test
    @DisplayName("프로그램 조회수 상위 10개 조회")
    @Transactional
    public void getTopTenProgram() throws Exception {
         initProgramTestData();
        this.mockMvc.perform(get("/api/programs/top10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("[0]..name").value("부산 여행 20"))
                .andExpect(jsonPath("[1]..name").value("부산 여행 19"))
                .andExpect(jsonPath("[2]..name").value("부산 여행 18"))
                .andExpect(jsonPath("[3]..name").value("부산 여행 17"))
                .andExpect(jsonPath("[4]..name").value("부산 여행 16"))
                .andExpect(jsonPath("[5]..name").value("부산 여행 15"))
                .andExpect(jsonPath("[6]..name").value("부산 여행 14"))
                .andExpect(jsonPath("[7]..name").value("부산 여행 13"))
                .andExpect(jsonPath("[8]..name").value("부산 여행 12"))
                .andExpect(jsonPath("[9]..name").value("부산 여행 11"));
    }


    @Test
    @DisplayName("프로그램 저장 정상 케이스")
    public void saveProgramTest() throws Exception {
        ProgramSaveDto programSaveDto = ProgramSaveDto.builder().name("여수 10미 먹거리")
                .region("전라남도 여수시").themeName("식도락여행")
                .introduction("여수시 일대 게장백반, 돌산갓김치등")
                .introductionDetail(
                        "여행자와 현지인이 꼽은 최고의 먹거리 여행지' 에서 대한민국 229개 지방자치단체 중 여수시가 1위에 선정되어 식도락 여행에 최적화된 프로그램")
                .build();
        this.mockMvc.perform(post("/api/programs/").contentType(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(programSaveDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("success").value("true"))
                .andExpect(jsonPath("message").value("저장 성공"))
                .andDo(write.document(
                        requestFields(
                                fieldWithPath("name").description("프로그램 이름"),
                                fieldWithPath("introduction").description("소개"),
                                fieldWithPath("introductionDetail").description("상세 소개"),
                                fieldWithPath("region").description("지역 이름"),
                                fieldWithPath("themeName").description("테마 이름")
                        )
                ));
    }

    @Test
    @DisplayName("프로그램 저장 name이 없는 케이스")
    public void saveProgramFailTest() throws Exception {
        ProgramSaveDto programSaveDto = ProgramSaveDto.builder()
                .region("전라남도 여수시").themeName("식도락여행")
                .introduction("여수시 일대 게장백반, 돌산갓김치등")
                .introductionDetail(
                        "여행자와 현지인이 꼽은 최고의 먹거리 여행지' 에서 대한민국 229개 지방자치단체 중 여수시가 1위에 선정되어 식도락 여행에 최적화된 프로그램")
                .build();
        this.mockMvc.perform(post("/api/programs/").contentType(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(programSaveDto)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("[0].field").value("name"))
                .andExpect(jsonPath("[0].objectName").value("programSaveDto"))
                .andExpect(jsonPath("[0].code").value("NotBlank"))
                .andExpect(jsonPath("[0].defaultMessage").value("must not be blank"));

    }

    @Test
    @DisplayName("프로그램 수정 정상 케이스")
    public void updateProgramTest() throws Exception {
        Program program = givenProgram(givenTheme("식도락여행"));
        ProgramSaveDto programSaveDto = ProgramSaveDto.builder().id(program.getId()).name("여수 10미 먹거리2")
                .region("전라남도 여수시").themeName("식도락여행2")
                .introduction(program.getIntroduction())
                .introductionDetail(program.getIntroductionDetail())
                .build();

        this.mockMvc.perform(put("/api/programs/").contentType(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(programSaveDto)))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(write.document(
                        requestFields(
                                fieldWithPath("id").description("id"),
                                fieldWithPath("name").description("프로그램 이름"),
                                fieldWithPath("introduction").description("소개"),
                                fieldWithPath("introductionDetail").description("상세 소개"),
                                fieldWithPath("region").description("지역 이름"),
                                fieldWithPath("themeName").description("테마 이름")
                        )));
    }

    @Test
    @DisplayName("프로그램 수정 없는 id로 요청 케이스")
    public void updateProgramFailTest() throws Exception {
        ProgramSaveDto programSaveDto = ProgramSaveDto.builder().id(10L)
                .name("여수 10미 먹거리2")
                .region("전라남도 여수시").themeName("식도락여행2")
                .introduction("여수시 일대 게장백반, 돌산갓김치등")
                .introductionDetail("여행자와 현지인이 꼽은 최고의 먹거리 여행지' 에서 대한민국 229개 지방자치단체 중 여수시가 1위에 선정되어 식도락 여행에 최적화된 프로그램")
                .build();
        this.mockMvc.perform(put("/api/programs/").contentType(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(programSaveDto)))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    private Program givenProgram(Theme theme) {
        Program program = Program.builder().name("여수 10미 먹거리")
                .region("전라남도 여수시")
                .introduction("여수시 일대 게장백반, 돌산갓김치등")
                .introductionDetail(
                        "여행자와 현지인이 꼽은 최고의 먹거리 여행지' 에서 대한민국 229개 지방자치단체 중 여수시가 1위에 선정되어 식도락 여행에 최적화된 프로그램")
                .theme(theme)
                .build();
        return this.programRepository.save(program);
    }

    private Theme givenTheme(String name) {
        return this.themeRepository.save(new Theme(name));
    }

    //프로그램 이름의 숫자가 높을수록 조회수가 높은 20개의 프로그램을 생성한다.
    private void initProgramTestData() {
        for (int i = 1; i <= 20; i++) {
            Program program = Program.builder().name("부산 여행 " + i)
                    .region("부산")
                    .introduction("부산의 꽃 축제")
                    .introductionDetail(
                            "꽃이 필 시기에 여행하기 좋은 프로그램")
                    .theme(new Theme("부산 벛꽃 투어" + i))
                    .build();
            programRepository.save(program);
        }

        for (int i = 3; i <= 22; i++) {
            for (int j = 0; j < i; j++) {
               programRepository.findById((long) i).orElseThrow(IllegalArgumentException::new).increaseReadCount();
            }
        }

    }
}