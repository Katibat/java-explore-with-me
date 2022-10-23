package ru.practicum.explorewithme.controller.pub;

import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explorewithme.client.StatsClient;
import ru.practicum.explorewithme.dto.event.EventFullDto;
import ru.practicum.explorewithme.dto.event.EventShortDto;
import ru.practicum.explorewithme.service.pub.EventPublicService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.*;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Публичный API для работы с событиями
 */

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/events")
public class EventPublicController {
    private final EventPublicService service;
    private final StatsClient client;

    @GetMapping // Получение событий с возможностью фильтрации
    public List<EventShortDto> getEvents(
            @RequestParam(required = false) String text,
            @RequestParam(required = false) List<Long> categories,
            @RequestParam(required = false) Boolean paid,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeStart,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeEnd,
            @RequestParam(defaultValue = "false") boolean onlyAvailable,
            @RequestParam(required = false) String sort,
            @PositiveOrZero @RequestParam(defaultValue = "0") int from,
            @Positive @RequestParam(defaultValue = "10") int size,
            HttpServletRequest request) {
        client.save(request);
        return service.getEventsSort(text, categories, paid, rangeStart, rangeEnd, onlyAvailable, sort, from, size);
    }

    @GetMapping("/{eventId}") // Получение подробной информации об опубликованном событии по его идентификатору
    public EventFullDto getEventById(@PathVariable Long eventId, HttpServletRequest request) {
        client.save(request);
        return service.findEventById(eventId);
    }

    @GetMapping("/{eventId}/comments") // Получить событие с отзывами + 1 hit в статистику
    public EventFullDto getEventWithComments(@PathVariable Long eventId,
                                             HttpServletRequest request) {
        client.save(request);
        return service.getEventWithAllComments(eventId);
    }
}