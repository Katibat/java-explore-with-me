package ru.practicum.explorewithme.compilation;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explorewithme.compilation.model.*;

import javax.validation.constraints.*;
import java.util.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/compilations")
public class CompilationPublicController {
    private final CompilationPublicService service;

    @GetMapping // Получение подборок событий
    public List<CompilationDto> findAll(@RequestParam(required = false) Boolean pinned,
                                        @PositiveOrZero @RequestParam(defaultValue = "0") int from,
                                        @Positive @RequestParam(defaultValue = "10") int size) {
        return service.findAll(pinned, from, size);
    }

    @GetMapping("/{compId}") // Получение подборки событий по идентификатору
    public CompilationDto findById(@PathVariable Long compId) {
        return service.findById(compId);
    }
}