package com.github.homework.program.service;

import com.github.homework.program.domain.Program;
import com.github.homework.program.exception.ProgramNotFoundException;
import com.github.homework.program.model.ProgramSaveDto;
import com.github.homework.program.repository.ProgramRepository;
import com.github.homework.theme.domain.Theme;
import com.github.homework.theme.service.ThemeService;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProgramSaveServiceImpl implements ProgramSaveService {

    private final ThemeService themeService;
    private final ProgramRepository programRepository;

    @Override
    @Transactional
    public void saveProgram(ProgramSaveDto programSaveDto) {
        Theme theme = this.themeService.getOrSaveTheme(programSaveDto.getThemeName());
        Program program = Program.builder()
            .name(programSaveDto.getName()).introduction(programSaveDto.getIntroduction())
            .introductionDetail(programSaveDto.getIntroductionDetail())
            .region(programSaveDto.getRegion())
            .theme(theme).build();
        theme.addProgram(program);
        this.programRepository.save(program);
    }


    @Override
    @Transactional
    public void updateProgram(ProgramSaveDto programSaveDto) throws ProgramNotFoundException {
        Program program = this.programRepository.findById(programSaveDto.getId()).orElseThrow(
            ProgramNotFoundException::new);
        Theme theme = this.themeService.getOrSaveTheme(programSaveDto.getThemeName());
        program.updateProgram(programSaveDto.getName(),
            programSaveDto.getIntroduction(),
            programSaveDto.getIntroductionDetail(),
            programSaveDto.getRegion(),
            theme
        );
    }


}
