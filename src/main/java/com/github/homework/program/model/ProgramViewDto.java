package com.github.homework.program.model;

import lombok.Getter;

@Getter
public class ProgramViewDto {

    private Long id;
    private String name;
    private String introduction;
    private String introductionDetail;
    private String region;
    private String themeName;

    public ProgramViewDto(Long id, String name, String introduction, String introductionDetail, String region, String themeName) {
        this.id = id;
        this.name = name;
        this.introduction = introduction;
        this.introductionDetail = introductionDetail;
        this.region = region;
        this.themeName = themeName;
    }
}
