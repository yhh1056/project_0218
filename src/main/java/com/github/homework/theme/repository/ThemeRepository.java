package com.github.homework.theme.repository;

import com.github.homework.theme.domain.Theme;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ThemeRepository extends JpaRepository<Theme, Long> {
    Optional<Theme> findByName(String name);
}