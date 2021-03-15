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
        return getProgramViewDto(programRepository.findById(id));
    }

    private Optional<ProgramViewDto> getProgramViewDto(Optional<Program> byId) {
        Program program = byId.get();
        program.increaseReadCount();
        return createProgramViewDto(Optional.of(program));
    }

    private Optional<ProgramViewDto> createProgramViewDto(Optional<Program> byId) {
        return byId.map(p ->
                new ProgramViewDto(
                        p.getId(),
                        p.getName(),
                        p.getIntroduction(),
                        p.getIntroductionDetail(),
                        p.getRegion()
                )
        );
    }

    @Override
    public Page<ProgramViewDto> pageBy(Pageable pageable) {
        return programRepository.findBy(pageable);
    }

    @Override
    public Optional<ProgramViewDto> getByProgramName(String name) {
        return getProgramViewDto(programRepository.findByName(name));
    }

    @Override
    public List<ProgramViewDto> getTopTenPrograms() {
        List<Program> topTenPrograms = programRepository.findTop10ByOrderByReadCountDesc();
        return topTenPrograms.stream()
                .map(p -> new ProgramViewDto(
                        p.getId(),
                        p.getName(),
                        p.getIntroduction(),
                        p.getIntroductionDetail(),
                        p.getRegion()
        )).collect(Collectors.toList());
    }

}
