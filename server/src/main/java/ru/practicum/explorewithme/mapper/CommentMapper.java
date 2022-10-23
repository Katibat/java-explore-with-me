package ru.practicum.explorewithme.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.explorewithme.dto.comment.CommentCreateDto;
import ru.practicum.explorewithme.dto.comment.CommentDto;
import ru.practicum.explorewithme.model.comment.Comment;
import ru.practicum.explorewithme.model.event.Event;
import ru.practicum.explorewithme.model.user.User;
import ru.practicum.explorewithme.service.priv.RequestPrivateService;

@Component
@RequiredArgsConstructor
public class CommentMapper {
    private final RequestPrivateService service;

    public CommentDto toDto(Comment comment) {
        return CommentDto.builder()
                .id(comment.getId())
                .text(comment.getText())
                .eventId(comment.getEvent().getId())
                .authorId(comment.getAuthor().getId())
                .edited(comment.getEdited())
                .created(comment.getCreated())
                .build();
    }

    public Comment toModel(CommentDto commentDto, Long userId) {
        User user = service.findUserById(userId);
        Event event = service.findEventById(commentDto.getEventId());
        return Comment.builder()
                .id(commentDto.getId())
                .text(commentDto.getText())
                .event(event)
                .author(user)
                .edited(commentDto.getEdited())
                .created(commentDto.getCreated())
                .build();
    }

    public Comment toNewModel(CommentCreateDto commentCreateDto, User user, Event event) {
        return Comment.builder()
                .text(commentCreateDto.getText())
                .event(event)
                .author(user)
                .edited(commentCreateDto.getEdited())
                .created(commentCreateDto.getCreated())
                .build();
    }
}