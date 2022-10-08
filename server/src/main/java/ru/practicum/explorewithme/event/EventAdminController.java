package ru.practicum.explorewithme.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explorewithme.event.model.*;

import javax.validation.constraints.*;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/admin/events")
public class EventAdminController {
    private final EventAdminService service;

    @GetMapping // Поиск событий
    private List<EventFullDto> searchEvents(
            @RequestParam(required = false) List<Long> users,
            @RequestParam(required = false) List<String> states,
            @RequestParam(required = false) List<Long> categories,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeStart,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeEnd,
            @PositiveOrZero @RequestParam(defaultValue = "0") int from,
            @Positive @RequestParam(defaultValue = "10") int size) {
        return service.searchEvents(users, states, categories, rangeStart, rangeEnd, from, size);
    }

    @PutMapping("/{eventId}") // Редактирование события
    private EventFullDto updateEventById(@RequestBody EventAdminUpdate eventAdminUpdate,
                                         @PathVariable Long eventId) {
        return service.update(eventAdminUpdate, eventId);
    }

    @PatchMapping("/{eventId}/publish") // Публикация события
    public EventFullDto publishEvent(@PathVariable Long eventId) {
        return service.changeEventState(eventId, true);
    }

    @PatchMapping("/{eventId}/reject") // Отклонение события
    public EventFullDto rejectEvent(@PathVariable Long eventId) {
        return service.changeEventState(eventId, false);
    }
}