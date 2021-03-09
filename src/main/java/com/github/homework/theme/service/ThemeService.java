package com.github.homework.theme.service;

import com.github.homework.program.exception.ProgramNotFoundException;
import com.github.homework.theme.domain.Theme;

public interface ThemeService {
    Theme getOrSaveTheme(String themeName);

    Theme getTheme(String themeName) throws ProgramNotFoundException;
}
