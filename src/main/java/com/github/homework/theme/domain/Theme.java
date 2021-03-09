package com.github.homework.theme.domain;


import com.github.homework.program.domain.Program;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.*;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SequenceGenerator(
    name = "theme_seq_generator",
    sequenceName = "theme_seq", allocationSize = 10)
@EqualsAndHashCode(of = {"id", "name"})
@ToString(of = {"id","name"})
public class Theme {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
        generator = "theme_seq_generator")
    private Long id;

    @Column(unique = true)
    private String name;

    public Theme(String name) {
        assert !name.isEmpty() : "empty name";
        this.name = name;
    }

    @OneToMany(mappedBy = "theme", cascade = CascadeType.ALL)
    private List<Program> programs = new ArrayList<>();

    public void addProgram(Program program) {
        programs.add(program);
        program.setTheme(this);
    }
}
