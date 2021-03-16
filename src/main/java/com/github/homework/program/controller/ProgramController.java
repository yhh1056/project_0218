package com.github.homework.program.controller;

import com.github.homework.program.exception.ProgramNotFoundException;
import com.github.homework.program.model.ProgramSaveDto;
import com.github.homework.program.model.ProgramViewDto;
import com.github.homework.program.model.SimpleResponse;
import com.github.homework.program.service.ProgramSaveService;
import com.github.homework.program.service.ProgramViewService;

import java.util.List;
import java.util.Optional;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/programs")
@RequiredArgsConstructor
public class ProgramController {

    private final ProgramViewService programViewService;
    private final ProgramSaveService programSaveService;

    @GetMapping
    public ResponseEntity<Page<ProgramViewDto>> pageBy(
            @PageableDefault(sort = "id", direction = Sort.Direction.DESC)
                    Pageable pageable) {
        return ResponseEntity.ok(this.programViewService.pageBy(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProgramViewDto> getBy(@PathVariable Long id) {
        Optional<ProgramViewDto> programViewDto = this.programViewService.getBy(id);
        try {
            this.programSaveService.increaseReadCount(id);
        } catch (ProgramNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
        return programViewDto.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<ProgramViewDto> getByProgramName(@PathVariable String name) {
        Optional<ProgramViewDto> programViewDto = this.programViewService.getByProgramName(name);
        try {
            this.programSaveService.increaseReadCount(name);
        } catch (ProgramNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
        return programViewDto.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/top10")
    public ResponseEntity<List<ProgramViewDto>> getTopTenPrograms() {
        List<ProgramViewDto> topTenPrograms = programViewService.getTopTenPrograms();
        if (topTenPrograms.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(topTenPrograms);
    }

    @PostMapping
    public ResponseEntity<SimpleResponse> saveProgram(@RequestBody @Valid ProgramSaveDto
                                                              programSaveDto) {
        this.programSaveService.saveProgram(programSaveDto);
        return ResponseEntity.ok(new SimpleResponse(true, "저장 성공"));
    }

    @PutMapping
    public ResponseEntity<SimpleResponse> updateProgram(@RequestBody @Valid ProgramSaveDto
                                                                programSaveDto) {
        try {
            this.programSaveService.updateProgram(programSaveDto);
        } catch (ProgramNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(new SimpleResponse(true, "수정 성공"));
    }
}
