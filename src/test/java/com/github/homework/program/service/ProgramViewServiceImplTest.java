package com.github.homework.program.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.BDDAssertions.then;
import static org.mockito.BDDMockito.given;

import com.github.homework.program.domain.Program;
import com.github.homework.program.model.ProgramViewDto;
import com.github.homework.program.repository.ProgramRepository;
import com.github.homework.theme.domain.Theme;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

@ExtendWith(MockitoExtension.class)
class ProgramViewServiceImplTest {

    @Mock
    private ProgramRepository programRepository;

    @InjectMocks
    private ProgramViewServiceImpl programViewService;

    @Test
    @DisplayName("프로그램이 한개 일때")
    void getByTest() {
        //given
        Program program = Program.builder()
                .name("name")
                .introduction("introduction")
                .introductionDetail("introductionDetail")
                .region("region")
                .theme(new Theme("theme"))
                .build();

        given(programRepository.findById(1L)).willReturn(Optional.of(program));
        //when
        Optional<ProgramViewDto> optionalProgramViewDto = programViewService.getBy(1L);
        //then
        then(optionalProgramViewDto).hasValueSatisfying(programViewDto -> {
                    then(programViewDto.getName()).isEqualTo("name");
                    then(programViewDto.getIntroduction()).isEqualTo("introduction");
                    then(programViewDto.getIntroductionDetail()).isEqualTo("introductionDetail");
                    then(programViewDto.getRegion()).isEqualTo("region");
                    then(programViewDto.getThemeName()).isEqualTo("theme");
                }
        );

    }

    @Test
    @DisplayName("프로그램이 여러개 일때")
    void pageByTest() {
        //given
        ProgramViewDto programViewDto = new ProgramViewDto(1L, "name", "introduction", "introductionDetail", "region", "themeName");
        given(programRepository.findBy(PageRequest.of(0, 100)))
                .willReturn(
                        new PageImpl<>(List.of(programViewDto, programViewDto))
                );

        //when
        Page<ProgramViewDto> programViewDtos = programViewService.pageBy(PageRequest.of(0, 100));
        //then
        then(programViewDtos.getContent()).hasSize(2);
        then(programViewDtos.getContent()).allSatisfy(p -> {
                    then(p.getId()).isGreaterThan(0L);
                    then(p.getName()).isEqualTo("name");
                    then(p.getIntroduction()).isEqualTo("introduction");
                    then(p.getIntroductionDetail()).isEqualTo("introductionDetail");
                    then(p.getRegion()).isEqualTo("region");
                    then(p.getThemeName()).isEqualTo("themeName");
                }
        );
    }

    @Test
    @DisplayName("테마를 조회하여 프로그램을 가져온다.")
    void getByTheme() {
        //given
        Theme theme = new Theme("theme");
        Program program = Program.builder()
                .name("name")
                .introduction("introduction")
                .introductionDetail("introductionDetail")
                .region("region")
                .theme(theme)
                .build();

        given(programRepository.findByTheme(theme)).willReturn(List.of(program));
        //when
        List<ProgramViewDto> programViewDtos = programViewService.getByTheme(theme);
        //then
        assertThat(programViewDtos.size()).isEqualTo(1);
        assertThat(programViewDtos.get(0).getName()).isEqualTo("name");
        assertThat(programViewDtos.get(0).getIntroduction()).isEqualTo("introduction");
        assertThat(programViewDtos.get(0).getIntroductionDetail()).isEqualTo("introductionDetail");
        assertThat(programViewDtos.get(0).getRegion()).isEqualTo("region");
        assertThat(programViewDtos.get(0).getThemeName()).isEqualTo("theme");
    }
}