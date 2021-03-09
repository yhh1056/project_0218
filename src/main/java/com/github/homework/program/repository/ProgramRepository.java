package com.github.homework.program.repository;

import com.github.homework.program.domain.Program;
import com.github.homework.theme.domain.Theme;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProgramRepository extends JpaRepository<Program, Long>, ProgramCustomRepository {

    List<Program> findByTheme(Theme theme);
}