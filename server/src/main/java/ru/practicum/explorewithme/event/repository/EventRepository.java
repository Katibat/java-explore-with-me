package ru.practicum.explorewithme.event.repository;

import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import ru.practicum.explorewithme.event.model.Event;
import ru.practicum.explorewithme.event.EventState;

import java.util.*;

@Repository
public interface EventRepository extends JpaRepository<Event, Long>, EventRepositoryCustom {
    Page<Event> findAllByInitiatorId(Long userId, Pageable pageable);

    @Modifying
    @Query("update Event e set e.state = ?1 where e.id = ?2")
    void cancelEvent(EventState state, Long eventId);
}