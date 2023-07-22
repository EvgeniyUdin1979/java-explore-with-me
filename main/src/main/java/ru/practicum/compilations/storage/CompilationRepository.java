package ru.practicum.compilations.storage;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.compilations.model.Compilation;

public interface CompilationRepository extends JpaRepository<Compilation, Long> {

    Long deleteById(long id);
}
