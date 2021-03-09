package com.github.homework.theme.service;

import com.github.homework.program.domain.Program;
import com.github.homework.program.exception.ProgramNotFoundException;
import com.github.homework.program.model.ProgramViewDto;
import com.github.homework.theme.domain.Theme;
import com.github.homework.theme.repository.ThemeRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ThemeServiceImpl implements ThemeService {

    private final ThemeRepository themeRepository;

    @Override
    @Transactional
    public Theme getOrSaveTheme(String themeName) {
        return this.themeRepository
            .findByName(themeName)
            .orElseGet(() -> this.themeRepository.save(new Theme(themeName)));
    }

    public List<ProgramViewDto> getByName(String name) throws ProgramNotFoundException {
        Theme theme = themeRepository.findByName(name).orElseThrow(ProgramNotFoundException::new);
        List<Program>programs = theme.getPrograms();
        return programs.stream()
                .map(program -> new ProgramViewDto(
                        program.getId(),
                        program.getName(),
                        program.getIntroduction(),
                        program.getIntroductionDetail(),
                        program.getRegion(),
                        program.getTheme().getName()
                        )).collect(Collectors.toList());
    }
}
