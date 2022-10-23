package ru.practicum.explorewithme.comment;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureTestEntityManager;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.explorewithme.dto.comment.CommentDto;
import ru.practicum.explorewithme.mapper.CommentMapper;
import ru.practicum.explorewithme.model.category.Category;
import ru.practicum.explorewithme.model.comment.Comment;
import ru.practicum.explorewithme.model.event.Event;
import ru.practicum.explorewithme.model.event.EventState;
import ru.practicum.explorewithme.model.event.Location;
import ru.practicum.explorewithme.model.user.User;
import ru.practicum.explorewithme.service.pub.CommentPublicService;

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
class CommentPublicServiceTest {

    @Autowired
    private final CommentPublicService service;

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
    public void getAllCommentsByEventsTest() {
        int commentCount = 5;
        Event event = generateAndPersistEvent();
        List<CommentDto> comments = generateAndPersistCommentsForEvent(commentCount, event)
                .stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
        List<CommentDto> actual = service.findAll(0, 10);
        assertEquals(comments.size(), actual.size());
    }

    @Test
    public void getCommentById() {
        Comment comment = generateAndPersistComment();
        CommentDto expected = mapper.toDto(comment);
        CommentDto actual = service.findCommentById(comment.getId());
        assertEquals(expected, actual);
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