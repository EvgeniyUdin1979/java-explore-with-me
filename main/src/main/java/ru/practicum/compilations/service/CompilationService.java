package ru.practicum.compilations.service;

import ru.practicum.compilations.dto.CompilationInDto;
import ru.practicum.compilations.dto.CompilationOutDto;

import java.util.List;
import java.util.Optional;

public interface CompilationService {

    CompilationOutDto addForAdmin(CompilationInDto inDto);

    CompilationOutDto updateByIdForAdmin(CompilationInDto inDto, long campId);

    void deleteByIdForAdmin(long compId);

    List<CompilationOutDto> getAllForPublic(Optional<Boolean> pinned, int from, int size);

    CompilationOutDto findById(long compId);
}
