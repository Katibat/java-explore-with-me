package ru.practicum.explorewithme.service.pub;

import ru.practicum.explorewithme.dto.event.EventFullDto;
import ru.practicum.explorewithme.dto.event.EventShortDto;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Реализация публичного API для работы с событиями
 */

public interface EventPublicService {

    /**
     * Получить событие с возможностью фильтрации по параметрам и с возможностью фильтрации
     * StatsClient передает на сервер статистики HttpRequest для сохранения нового EndpointHit в таблице Stats
     * ViewStats увеличен на 1 просмотр
     * @param text текст для поиска по аннотациями и описанию событий
     * @param categories список идентификаторов категорий в которых будет вестись поиск
     * @param paid true - платные события, false - бесплатные события
     * @param rangeStart дата и время начала периода поиска
     * @param rangeEnd дата и время окончания периода поиска
     * @param onlyAvailable true - исчерпан лимит запросов на участие, false - не исчерпан лимит запросов на участие
     * @param sort сортировка: EVENT_DATE - по дате события, VIEWS - по количеству просмотров
     * @param from количество объектов, default value = 0
     * @param size размер страницы default value = 10
     * @return List<EventShortDto>
     */
    List<EventShortDto> getEventsSort(String text,
                                      List<Long> categories,
                                      Boolean paid,
                                      LocalDateTime rangeStart,
                                      LocalDateTime rangeEnd,
                                      boolean onlyAvailable,
                                      String sort,
                                      int from,
                                      int size);

    /**
     * Получить полную информацию об опубликованном событии по идентификатору
     * StatsClient передает на сервер статистики HttpRequest для сохранения нового EndpointHit в таблице Stats
     * ViewStats увеличен на 1 просмотр
     * @param eventId идентификатор события
     * @return EventFullDto
     */
    EventFullDto findEventById(Long eventId);

    /**
     * Получить полную информацию о событии со списком отзывов и добавить 1 hit в Stats
     * @param eventId идентификатор события
     * @return EventFullDto
     */
    EventFullDto getEventWithAllComments(Long eventId);
}