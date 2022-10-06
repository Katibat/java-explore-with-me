package ru.practicum.explorewithme.event.repository;

import ru.practicum.event.model.Event;
import ru.practicum.event.EventState;
import ru.practicum.explorewithme.event.EventState;
import ru.practicum.explorewithme.event.model.Event;

import java.time.LocalDateTime;
import java.util.List;

public interface EventRepositoryCustom {
    List<Event> findEventsByPublicFilters(String text,
                                          Long[] categories,
                                          Boolean paid,
                                          LocalDateTime rangeStart,
                                          LocalDateTime rangeEnd,
                                          boolean onlyAvailable);

    List<Event> findEventsByAdminFilters(Long[] userIds,
                                         EventState[] states,
                                         Long[] categories,
                                         LocalDateTime rangeStart,
                                         LocalDateTime rangeEnd);
}