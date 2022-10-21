package ru.practicum.explorewithme.controller.pub;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explorewithme.client.StatsClient;
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
@RequestMapping(path = "/comments")
public class CommentPublicController {
    private final CommentPublicService service;

    @GetMapping // Получение списка отзывов на события
    public List<CommentDto> findAll(@PositiveOrZero @RequestParam(defaultValue = "0") int from,
                                     @Positive @RequestParam(defaultValue = "10") int size) {
        return service.findAll(from, size);
    }

    @GetMapping("/{commentId}")
    public CommentDto getEventComment(@PathVariable Long commentId) {
        return service.findCommentById(commentId);
    }
}