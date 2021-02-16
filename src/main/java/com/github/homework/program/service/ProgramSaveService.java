package com.github.homework.program.service;

import com.github.homework.program.exception.ProgramNotFoundException;
import com.github.homework.program.model.ProgramSaveDto;

public interface ProgramSaveService {

    void saveProgram(ProgramSaveDto programSaveDto);

    void updateProgram(ProgramSaveDto programSaveDto) throws ProgramNotFoundException;
}
