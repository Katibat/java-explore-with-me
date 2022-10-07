package ru.practicum.explorewithme.request;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.explorewithme.request.model.Request;
import ru.practicum.explorewithme.user.model.User;

import java.util.List;

@Repository
public interface RequestRepository extends JpaRepository<Request, Long> {
    List<Request> findAllByRequesterId(User user);

    List<Request> findAllByEventId(Long eventId);

    @Query(value = "select count(*) from requests where event_id = ?1 and status = 'CONFIRMED'", nativeQuery = true)
    Integer getConfirmedRequests(Long eventId);
}