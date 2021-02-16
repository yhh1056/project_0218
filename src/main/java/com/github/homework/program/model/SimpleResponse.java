package com.github.homework.program.model;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SimpleResponse {
    private boolean success;
    private String message;
}
