package ru.practicum.explorewithme.controller.pub;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explorewithme.client.StatsClient;
import ru.practicum.explorewithme.dto.comment.CommentDto;
import ru.practicum.explorewithme.dto.event.EventFullDto;
import ru.practicum.explorewithme.service.pub.CommentPublicService;

import javax.servlet.http.HttpServletRequest;

/**
 * Публичный API для просмотра отзывов на события
 */

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/events")
public class CommentPublicController {
    private final CommentPublicService service;
    private final StatsClient client;

    @GetMapping("/{eventId}/comments") // Получить событие с отзывами + 1 hit в статистику
    public EventFullDto getEventWithComments(@PathVariable Long eventId,
                                             HttpServletRequest request) {
        client.save(request);
        return service.getEventWithAllComments(eventId);
    }

    @GetMapping("/{eventId}/comments/{commentId}")
    public CommentDto getEventComment(@PathVariable Long eventId,
                                      @PathVariable Long commentId) {
        return service.findCommentById(eventId, commentId);
    }
}