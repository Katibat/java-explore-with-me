package ru.practicum.explorewithme.comment;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureTestEntityManager;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.explorewithme.dto.comment.CommentCreateDto;
import ru.practicum.explorewithme.dto.comment.CommentDto;
import ru.practicum.explorewithme.dto.comment.CommentUpdateDto;
import ru.practicum.explorewithme.exception.ForbiddenException;
import ru.practicum.explorewithme.mapper.CommentMapper;
import ru.practicum.explorewithme.model.category.Category;
import ru.practicum.explorewithme.model.comment.Comment;
import ru.practicum.explorewithme.model.event.Event;
import ru.practicum.explorewithme.model.event.EventState;
import ru.practicum.explorewithme.model.event.Location;
import ru.practicum.explorewithme.model.user.User;
import ru.practicum.explorewithme.service.priv.CommentPrivateService;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase
@AutoConfigureTestEntityManager
@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class CommentPrivateServiceTest {

    @Autowired
    private final CommentPrivateService service;

    @Autowired
    private final CommentMapper mapper;

    @Autowired
    private final TestEntityManager testEntityManager;

    private static AtomicLong userIdHolder;

    @BeforeAll
    public static void init() {
        userIdHolder = new AtomicLong();
    }

    @BeforeEach
    public void beforeEachCommentServiceTests() {
        testEntityManager.clear();
    }

    @Test
    public void saveCommentTest() {
        Event event = generateAndPersistEvent();
        User author = generateAndPersistUser();
        CommentCreateDto commentCreateDto = CommentCreateDto.builder()
                .text("Comment " + System.nanoTime())
                .edited(false)
                .created(LocalDateTime.now())
                .build();
        CommentDto saved = service.save(commentCreateDto, author.getId(), event.getId());
        CommentDto actual = mapper.toDto(testEntityManager.find(Comment.class, saved.getId()));
        assertEquals(saved.getText(), actual.getText());
    }

    @Test
    public void updateCommentTest() {
        Event event = generateAndPersistEvent();
        User author = generateAndPersistUser();
        CommentCreateDto commentCreateDto = CommentCreateDto.builder()
                .text("Comment " + System.nanoTime())
                .edited(false)
                .created(LocalDateTime.now())
                .build();
        CommentDto saved = service.save(commentCreateDto, author.getId(), event.getId());

        CommentUpdateDto commentUpdateDto = CommentUpdateDto.builder()
                .text("Comment update test")
                .build();
        CommentDto updated = service.update(commentUpdateDto, author.getId(), event.getId(), saved.getId());
        Comment found = testEntityManager.find(Comment.class, saved.getId());
        assertEquals(updated.getText(), found.getText());
    }

    @Test
    public void updateCommentFailNotAuthorTest() {
        Comment comment = generateAndPersistComment();
        User otherUser = generateAndPersistUser();
        CommentUpdateDto commentUpdateDto = CommentUpdateDto.builder()
                .text("Comment update test")
                .build();
        assertThrows(ForbiddenException.class,
                () -> service.update(commentUpdateDto, otherUser.getId(), comment.getEvent().getId(), comment.getId())
        );
    }

    @Test
    public void deleteCommentByAuthorTest() {
        Comment comment = generateAndPersistComment();
        service.delete(comment.getAuthor().getId(), comment.getEvent().getId(), comment.getId());
        assertNull(testEntityManager.find(Comment.class, comment.getId()));
    }

    @Test
    public void deleteCommentFailNotAuthorTest() {
        Comment comment = generateAndPersistComment();
        User otherUser = generateAndPersistUser();
        assertThrows(ForbiddenException.class,
                () -> service.delete(otherUser.getId(), comment.getEvent().getId(), comment.getId())
        );
    }

    @Test
    public void getAllCommentsByEventIdTest() {
        int commentQuantity = 5;
        Event event = generateAndPersistEvent();
        List<CommentDto> comments = generateAndPersistCommentsForEvent(commentQuantity, event)
                .stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
        List<CommentDto> expected = service.findCommentsByEventId(event.getInitiator().getId(), event.getId());
        assertEquals(comments.size(), expected.size());
    }

    private User generateAndPersistUser() {
        String email = String.format("email-%s@yandex.ru", userIdHolder.incrementAndGet());
        String name = String.format("user-%s", userIdHolder.get());
        User user = User.builder()
                .name(name)
                .email(email)
                .build();
        return testEntityManager.persist(user);
    }

    private Event generateAndPersistEvent() {
        long stamp = System.nanoTime();
        String annotation = "annotation-" + stamp;
        Category category = generateAndPersistEventCategory("Category-" + stamp);
        LocalDateTime createOn = LocalDateTime.now();
        String description = "description-" + stamp;
        LocalDateTime eventDate = LocalDateTime.now().plusMonths(1);
        User initiator = generateAndPersistUser();
        LocalDateTime publishedOn = LocalDateTime.now().plusHours(1);
        String title = "title-" + stamp;
        Location location = new Location(59.57f, 30.19f);
        Event event = Event.builder()
                .title(title)
                .annotation(annotation)
                .description(description)
                .category(category)
                .createdOn(createOn)
                .eventDate(eventDate)
                .initiator(initiator)
                .paid(false)
                .participantLimit(100)
                .publishedOn(publishedOn)
                .requestModeration(false)
                .state(EventState.PUBLISHED)
                .location(location)
                .build();
        return testEntityManager.persist(event);
    }

    private Category generateAndPersistEventCategory(String name) {
        Category category = Category.builder()
                .name(name)
                .build();
        return testEntityManager.persistAndFlush(category);
    }

    private Comment generateAndPersistComment() {
        Comment comment = Comment.builder()
                .text("Comment " + System.nanoTime())
                .event(generateAndPersistEvent())
                .author(generateAndPersistUser())
                .edited(false)
                .created(LocalDateTime.now())
                .build();
        return testEntityManager.persist(comment);
    }

    private List<Comment> generateAndPersistCommentsForEvent(int commentCount, Event event) {
        List<Comment> comments = new ArrayList<>();
        for (int i = 0; i < commentCount; i++) {
            Comment comment = Comment.builder()
                    .text("Comment " + System.nanoTime())
                    .event(event)
                    .author(generateAndPersistUser())
                    .edited(false)
                    .created(LocalDateTime.now())
                    .build();
            testEntityManager.persist(comment);
            comments.add(comment);
        }
        return comments;
    }
}