package ru.practicum.explorewithme.event;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explorewithme.event.model.*;
import ru.practicum.explorewithme.request.model.RequestFullDto;

import javax.validation.Valid;
import javax.validation.constraints.*;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/users")
public class EventPrivateController {
    private final EventPrivateService service;

    @GetMapping("/{userId}/events") // Получение событий добавленных текущим пользователем
    public List<EventShortDto> findEventsForInitiator(@PathVariable Long userId,
                                                      @PositiveOrZero @RequestParam(defaultValue = "0") int from,
                                                      @Positive @RequestParam(defaultValue = "10") int size) {
        return service.findEventByInitiatorId(userId, from, size);
    }

    @PatchMapping("/{userId}/events") // Изменение события добавленного текущим пользователем
    public EventFullDto update(@RequestBody EventUpdateDto eventUpdateDto, @PathVariable Long userId) {
        return service.update(eventUpdateDto, userId);
    }

    @PostMapping("/{userId}/events") // Добавление нового события
    public EventFullDto save(@Valid @RequestBody EventCreateDto eventCreateDto, @PathVariable Long userId) {
        return service.save(eventCreateDto, userId);
    }

    @GetMapping("/{userId}/events/{eventId}") // Получение полной информации о событии добавленном тек.пользователем
    public EventFullDto getEventInfo(@PathVariable Long userId, @PathVariable Long eventId) {
        return service.findEventInfoByInitiator(userId, eventId);
    }

    @PatchMapping("/{userId}/events/{eventId}") // Отмена события добавленного текущим пользователем
    public EventFullDto cancelEvent(@PathVariable Long userId, @PathVariable Long eventId) {
        return service.cancel(userId, eventId);
    }

    @GetMapping("/{userId}/events/{eventId}/requests") // Получение информации о запросах на участие
    // в событии текущего пользователя
    public List<RequestFullDto> getAllRequests(@PathVariable Long userId, @PathVariable Long eventId) {
        return service.getAllRequestsByEventId(userId, eventId);
    }

    @PatchMapping("/{userId}/events/{eventId}/requests/{requestId}/confirm") // Подтверждение чужой заявки на участие
    public RequestFullDto confirmRequest(@PathVariable Long userId,
                                         @PathVariable Long eventId,
                                         @PathVariable Long requestId) {
        return service.changeRequestStatus(userId, eventId, requestId, true);
    }

    @PatchMapping("/{userId}/events/{eventId}/requests/{requestId}/reject") // Отклонение чужой заявки на участие
    public RequestFullDto rejectRequest(@PathVariable Long userId,
                                        @PathVariable Long eventId,
                                        @PathVariable Long requestId) {
        return service.changeRequestStatus(userId, eventId, requestId, false);
    }
}