package ru.practicum.explorewithme.event.repository;

import ru.practicum.event.model.Event;
import ru.practicum.event.EventState;
import ru.practicum.explorewithme.event.EventState;
import ru.practicum.explorewithme.event.model.Event;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.*;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class EventRepositoryCustomImpl implements EventRepositoryCustom {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Event> findEventsByPublicFilters(String text,
                                                 Long[] categories,
                                                 Boolean paid,
                                                 LocalDateTime rangeStart,
                                                 LocalDateTime rangeEnd,
                                                 boolean onlyAvailable) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Event> query = cb.createQuery(Event.class);
        Root<Event> event = query.from(Event.class);
        List<Predicate> predicates = new ArrayList<>();
        Path<String> status = event.get("state");
        predicates.add(cb.equal(status, EventState.PUBLISHED));
        handleCategories(categories, event, predicates);
        handleTimeFilter(cb, event, predicates, rangeStart, rangeEnd);
        if (paid != null) {
            Path<Boolean> paidPath = event.get("paid");
            predicates.add(cb.isTrue(paidPath));
        }
        query.select(event).where(predicates.toArray(new Predicate[0]));
        List<Event> events = entityManager.createQuery(query).getResultList();
        return events.stream()
                .filter(e -> filterByAvailable(e, onlyAvailable))
                .filter(e -> filterByText(e, text))
                .collect(Collectors.toList());
    }

    @Override
    public List<Event> findEventsByAdminFilters(Long[] userIds,
                                                EventState[] states,
                                                Long[] categories,
                                                LocalDateTime rangeStart,
                                                LocalDateTime rangeEnd) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Event> query = cb.createQuery(Event.class);
        Root<Event> event = query.from(Event.class);
        List<Predicate> predicates = new ArrayList<>();
        handleCategories(categories, event, predicates);
        handleTimeFilter(cb, event, predicates, rangeStart, rangeEnd);
        if (userIds != null) {
            Path<String> userIdPath = event.get("initiatorId");
            predicates.add(userIdPath.in((Object[]) userIds));
        }
        if (states != null) {
            Path<String> statesPath = event.get("state");
            predicates.add(statesPath.in((Object[]) states));
        }
        query.select(event).where(predicates.toArray(new Predicate[0]));
        return entityManager.createQuery(query).getResultList();
    }

    private void handleCategories(Long[] categories, Root<Event> event, List<Predicate> predicates) {
        if (categories != null) {
            Path<String> categoryId = event.get("category");
            predicates.add(categoryId.in((Object[]) categories));
        }
    }

    private void handleTimeFilter(CriteriaBuilder cb,
                                  Root<Event> event,
                                  List<Predicate> predicates,
                                  LocalDateTime rangeStart,
                                  LocalDateTime rangeEnd) {
        Path<LocalDateTime> datePath = event.get("eventDate");
        if (rangeStart != null && rangeEnd != null) {
            predicates.add(cb.between(datePath, rangeStart, rangeEnd));
        } else if (rangeStart != null) {
            predicates.add(cb.greaterThan(datePath, rangeStart));
        } else if (rangeEnd != null) {
            predicates.add(cb.lessThan(datePath, rangeEnd));
        }
    }

    private boolean filterByAvailable(Event event, boolean isAvailable) {
        if (isAvailable) {
            int participantLimit = event.getParticipantLimit();
            if (participantLimit == 0) {
                return true;
            }
            long confirmedRequests = event.getConfirmedRequests();
            return participantLimit > confirmedRequests;
        } else {
            return true;
        }
    }

    private boolean filterByText(Event event, String text) {
        if (text == null) {
            return true;
        }
        String description = event.getDescription().toLowerCase(Locale.ROOT);
        String annotation = event.getAnnotation().toLowerCase(Locale.ROOT);
        text = text.toLowerCase(Locale.ROOT);
        return description.contains(text) || annotation.contains(text);
    }
}