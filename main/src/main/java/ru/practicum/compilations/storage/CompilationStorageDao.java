package ru.practicum.compilations.storage;

import ru.practicum.compilations.model.Compilation;

import java.util.List;
import java.util.Optional;

public interface CompilationStorageDao {

    Compilation add(Compilation compilation);

    Compilation update(Compilation compilation);

    Long deleteById(long id);

    Optional<Compilation> findById(long id);

    List<Compilation> findAll(Optional<Boolean> pinned, int from, int size);
}
