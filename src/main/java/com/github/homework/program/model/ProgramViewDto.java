package com.github.homework.program.model;

import com.github.homework.program.domain.Program;
import com.github.homework.theme.domain.Theme;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
public class ProgramViewDto {

    private Long id;
    private String name;
    private String introduction;
    private String introductionDetail;
    private String region;
    private String themeName;

    @Builder
    public ProgramViewDto(Long id, String name, String introduction, String introductionDetail, String region, String themeName) {
        this.id = id;
        this.name = name;
        this.introduction = introduction;
        this.introductionDetail = introductionDetail;
        this.region = region;
        this.themeName = themeName;
    }

    public static ProgramViewDto create(Program program) {
        final Theme theme = program.getTheme();
        return new ProgramViewDto(
                program.getId(),
                program.getName(),
                program.getIntroduction(),
                program.getIntroductionDetail(),
                program.getRegion(),
                theme.getName());
    }
}
