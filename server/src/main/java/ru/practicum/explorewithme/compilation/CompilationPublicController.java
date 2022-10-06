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
    private final CompilationPublicService compilationService;

    @GetMapping // Получение подборок событий
    public List<CompilationDto> findAll(@RequestParam(required = false) Boolean pinned,
                                        @PositiveOrZero @RequestParam(defaultValue = "0") int from,
                                        @Positive @RequestParam(defaultValue = "10") int size) {
        return compilationService.findAll(pinned, from, size);
    }

    @GetMapping("/{compilationId}") // Получение подборки событий по идентификатору
    public CompilationDto findById(@PathVariable Long compilationId) {
        return compilationService.findById(compilationId);
    }
}