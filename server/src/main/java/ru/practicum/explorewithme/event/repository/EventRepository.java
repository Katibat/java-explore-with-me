package ru.practicum.explorewithme.event.repository;

import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.*;
import ru.practicum.explorewithme.event.model.Event;
import ru.practicum.event.EventState;
import ru.practicum.explorewithme.event.EventState;

import java.util.*;

public interface EventRepository extends JpaRepository<Event, Long>, EventRepositoryCustom {
    Page<Event> findAllByInitiatorId(Long userId, Pageable pageable);

    List<Event> findAllByState(EventState state);

    List<Event> findAllByStateAndPaid(EventState state, boolean paid);

    Event findEventByInitiatorId(Long userId);

    @Modifying
    @Query("update Event e set e.state = ?1 where e.id = ?2")
    void cancelEvent(EventState state, Long eventId);
    int countByCategoryId(Long categoryId);
}