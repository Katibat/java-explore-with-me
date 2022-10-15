package ru.practicum.explorewithme.service.admin;

import ru.practicum.explorewithme.dto.compilation.CompilationCreateDto;
import ru.practicum.explorewithme.dto.compilation.CompilationDto;

/**
 * Реализация API для работы с подборками событий на уровне администратора
 */

public interface CompilationAdminService {

    /**
     * Сохранить новую подборку событий
     * @param compilationCreateDto
     * @return CompilationDto
     */
    CompilationDto save(CompilationCreateDto compilationCreateDto);

    /**
     * Удалить подборку событий
     * @param compId идентификатор подборки событий
     */
    void delete(Long compId);

    /**
     * Сохранить или удалить событие в подборке событий
     * @param compId идентификатор подборки событий
     * @param eventId идентификатор события
     * @param isDeleting true - удалить, false - сохранить
     */
    void saveOrDeleteEventInCompilation(Long compId, Long eventId, boolean isDeleting);

    /**
     * Закрепить или открепить подборку событий на главной странице
     * @param compId идентификатор подборки событий
     * @param isPin true - открепить, false - закрепить
     */
    void changeCompilationPin(Long compId, boolean isPin);
}