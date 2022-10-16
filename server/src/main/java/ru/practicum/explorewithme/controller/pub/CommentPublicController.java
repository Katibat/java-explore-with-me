package ru.practicum.explorewithme.controller.pub;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explorewithme.dto.comment.CommentDto;
import ru.practicum.explorewithme.service.pub.CommentPublicService;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

/**
 * Публичный API для просмотра отзывов на события
 */

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/events")
public class CommentPublicController {
    private final CommentPublicService service;

    @GetMapping("/{eventId}/comments") // Получить список комментариев на событие с параметрами
    public List<CommentDto> getEventComments(@PathVariable Long eventId,
                                             @PositiveOrZero @RequestParam(defaultValue = "0") int from,
                                             @Positive @RequestParam(defaultValue = "10") int size) {
        return service.findAllComments(eventId, from, size);
    }

    @GetMapping("/{eventId}/comments/{commentId}")
    public CommentDto getEventComment(@PathVariable Long eventId,
                                      @PathVariable Long commentId) {
        return service.findCommentById(eventId, commentId);
    }
}