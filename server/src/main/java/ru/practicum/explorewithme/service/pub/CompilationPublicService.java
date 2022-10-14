package ru.practicum.explorewithme.service.pub;

import ru.practicum.explorewithme.dto.compilation.CompilationDto;

import java.util.List;

/**
 * Реализация публичного API для работы с подборками событий
 */

public interface CompilationPublicService {

    /**
     * Получить подборку событий по параметрам
     * @param pinned true - закрепленные на главной странице, false - не закрепленные
     * @param from количество объектов, default value = 0
     * @param size размер страницы default value = 10
     * @return List<CompilationDto>
     */
    List<CompilationDto> findAll(Boolean pinned, int from, int size);

    /**
     * Получить подборку событий по идентификатору
     * @param compId идентификатор подборки событий
     * @return CompilationDto
     */
    CompilationDto findById(Long compId);
}