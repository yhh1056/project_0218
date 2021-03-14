package com.github.homework.program.service;

import com.github.homework.program.model.ProgramViewDto;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProgramViewService {

    Optional<ProgramViewDto> getBy(Long id);

    Page<ProgramViewDto> pageBy(Pageable pageable);

    Optional<ProgramViewDto> getByProgramName(String name);
}
