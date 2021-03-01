package com.github.homework.program.service;

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
        ProgramViewDto programViewDto = getProgramViewDto();
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
                    then(p.getThemeName()).isEqualTo("theme");
                }
        );
    }

    private ProgramViewDto getProgramViewDto() {
        return ProgramViewDto.builder()
                .id(1L)
                .name("name")
                .introduction("introduction")
                .introductionDetail("introductionDetail")
                .region("region")
                .themeName("theme")
                .build();
    }
}