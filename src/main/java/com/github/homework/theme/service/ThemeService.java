package com.github.homework.theme.service;

import com.github.homework.program.exception.ProgramNotFoundException;
import com.github.homework.program.model.ProgramViewDto;
import com.github.homework.theme.domain.Theme;
import java.util.List;

public interface ThemeService {
    Theme getOrSaveTheme(String themeName);

    List<ProgramViewDto> getByName(String name) throws ProgramNotFoundException;
}
