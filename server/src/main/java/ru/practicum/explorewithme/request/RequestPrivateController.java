package ru.practicum.explorewithme.request;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explorewithme.request.model.RequestFullDto;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/users")
public class RequestPrivateController {
    private final RequestPrivateService service;

    @GetMapping("/{userId}/requests") // Получение информации о заявках текущего пользователя
    // на участие в чужих событиях
    public List<RequestFullDto> getRequests(@PathVariable Long userId) {
        return  service.findRequests(userId);
    }

    @PostMapping("/{userId}/requests") // Добавление запроса от текущего пользователя на участие в событии
    public RequestFullDto saveRequest(@PathVariable Long userId, @RequestParam Long eventId) {
        return service.save(userId, eventId);
    }

    @PatchMapping("/{userId}/requests/{requestId}/cancel") // Отмена своего запроса на участие в событии
    public RequestFullDto cancelRequest(@PathVariable Long userId, @PathVariable Long requestId) {
        return service.cancelRequest(userId, requestId);
    }
}