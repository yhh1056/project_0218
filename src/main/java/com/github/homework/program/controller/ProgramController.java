package com.github.homework.program.controller;

import com.github.homework.program.exception.ProgramNotFoundException;
import com.github.homework.program.model.ProgramSaveDto;
import com.github.homework.program.model.ProgramViewDto;
import com.github.homework.program.model.SimpleResponse;
import com.github.homework.program.service.ProgramSaveService;
import com.github.homework.program.service.ProgramViewService;

import com.github.homework.theme.domain.Theme;
import com.github.homework.theme.service.ThemeService;
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
    private final ThemeService themeService;

    @GetMapping
    public ResponseEntity<Page<ProgramViewDto>> pageBy(
            @PageableDefault(sort = "id", direction = Sort.Direction.DESC)
                    Pageable pageable) {
        return ResponseEntity.ok(this.programViewService.pageBy(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProgramViewDto> getBy(@PathVariable Long id) {
        Optional<ProgramViewDto> programViewDto = this.programViewService.getBy(id);
        return programViewDto.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/theme/{name}")
    public ResponseEntity<List<ProgramViewDto>> getByThemeName(@PathVariable String name) {
        try {
            Theme theme = themeService.getTheme(name);

            return ResponseEntity.ok().body(programViewService.getByTheme(theme));
        } catch (ProgramNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
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
