package ru.practicum.explorewithme.request;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.explorewithme.event.model.Event;
import ru.practicum.explorewithme.request.model.Request;
import ru.practicum.explorewithme.user.model.User;

import java.util.List;

public interface RequestRepository extends JpaRepository<Request, Long> {
    List<Request> findAllByRequester(User user);

    List<Request> findAllByEvent(Event event);

    List<Request> findByEvent(Event event);

    @Query(value = "select count(*) from requests where event_id = ?1 and status = 'CONFIRMED'", nativeQuery = true)
    Integer getConfirmedRequests(Long eventId);
}