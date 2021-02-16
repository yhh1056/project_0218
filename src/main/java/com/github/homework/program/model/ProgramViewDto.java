package com.github.homework.program.model;

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

    public ProgramViewDto(Long id, String name, String introduction, String introductionDetail, String region) {
        this.id = id;
        this.name = name;
        this.introduction = introduction;
        this.introductionDetail = introductionDetail;
        this.region = region;
    }
}
