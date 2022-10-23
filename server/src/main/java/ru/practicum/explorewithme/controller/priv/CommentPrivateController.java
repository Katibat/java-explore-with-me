package ru.practicum.explorewithme.controller.priv;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explorewithme.dto.comment.CommentCreateDto;
import ru.practicum.explorewithme.dto.comment.CommentDto;
import ru.practicum.explorewithme.dto.comment.CommentUpdateDto;
import ru.practicum.explorewithme.service.priv.CommentPrivateService;

import javax.validation.Valid;
import java.util.List;

/**
 * Закрытый API для работы с отзывами на события (для авторизованных пользователей)
 */

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/users")
public class CommentPrivateController {
    private final CommentPrivateService service;

    @PostMapping("/{userId}/events/{eventId}/comments") // Добавление нового отзыва о событии текущим пользователем
    public CommentDto save(@Valid @RequestBody CommentCreateDto commentCreateDto,
                           @PathVariable Long userId,
                           @PathVariable Long eventId) {
        return service.save(commentCreateDto, userId, eventId);
    }

    // изменение отзыва на событие текущим пользователем
    @PatchMapping("/{userId}/events/{eventId}/comments/{commentId}")
    public CommentDto update(@Valid @RequestBody CommentUpdateDto commentUpdateDto,
                             @PathVariable Long userId,
                             @PathVariable Long eventId,
                             @PathVariable Long commentId) {
        return service.update(commentUpdateDto, userId, eventId, commentId);
    }

    @DeleteMapping("/{userId}/events/{eventId}/comments/{commentId}") // удаление отзыва на событие тек.пользователем
    public void delete(@PathVariable Long userId,
                                     @PathVariable Long eventId,
                                     @PathVariable Long commentId) {
        service.delete(userId, eventId, commentId);
    }

    @GetMapping("/{userId}/events/{eventId}/comments") // Получение списка отзывов на событие текущим пользователем
    public List<CommentDto> findCommentsByEventId(@PathVariable Long userId,
                                                  @PathVariable Long eventId) {
        return service.findCommentsByEventId(userId, eventId);
    }
}