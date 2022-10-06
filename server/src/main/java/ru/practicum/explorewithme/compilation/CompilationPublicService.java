package ru.practicum.explorewithme.compilation;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.explorewithme.compilation.model.*;
import ru.practicum.explorewithme.exception.NotFoundException;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CompilationPublicService {
    private final CompilationRepository repository;

    public List<CompilationDto> findAll(Boolean pinned, int from, int size) {
        if (pinned == null) {
            return repository.findAll(PageRequest.of(from, size))
                    .stream()
                    .map(CompilationMapper::toDto)
                    .collect(Collectors.toList());
        }
        return repository.findByPinned(pinned, PageRequest.of(from, size))
                .stream().map(CompilationMapper::toDto).collect(Collectors.toList());
    }

    public CompilationDto findById(Long compilationId) {
        Compilation compilation = repository.findById(compilationId)
                .orElseThrow(() -> new NotFoundException("CompilationPublicService: Не найдена подборка событий " +
                        "с id=" + compilationId));
        return CompilationMapper.toDto(compilation);
    }
}