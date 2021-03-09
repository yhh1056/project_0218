package com.github.homework.program.service;

import com.github.homework.program.domain.Program;
import com.github.homework.program.model.ProgramViewDto;
import com.github.homework.program.repository.ProgramRepository;
import com.github.homework.theme.domain.Theme;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProgramViewServiceImpl implements ProgramViewService {

    private final ProgramRepository programRepository;

    @Override
    public Optional<ProgramViewDto> getBy(Long id) {
        Optional<Program> byId = programRepository.findById(id);
        return byId.map(p ->
                getProgramViewDto(p)
        );
    }

    private ProgramViewDto getProgramViewDto(Program program) {
        return new ProgramViewDto(
                program.getId(),
                program.getName(),
                program.getIntroduction(),
                program.getIntroductionDetail(),
                program.getRegion(),
                program.getTheme().getName()
        );
    }

    @Override
    public Page<ProgramViewDto> pageBy(Pageable pageable) {
        return programRepository.findBy(pageable);
    }

    @Override
    public List<ProgramViewDto> getByTheme(Theme theme) {
        List<Program> programs = programRepository.findByTheme(theme);
        return programs.stream()
                .map(p -> getProgramViewDto(p))
                .collect(Collectors.toList());
    }
}
