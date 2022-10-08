package ru.practicum.explorewithme.request;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.explorewithme.request.model.Request;

import java.util.List;
import java.util.Optional;

@Repository
public interface RequestRepository extends JpaRepository<Request, Long> {
    List<Request> findAllByRequester(Long userId);

    List<Request> findAllByEvent(Long eventId);

    Integer countByEventAndStatus(Long eventId, RequestStatus status);

    @Modifying
    @Transactional
    @Query("UPDATE Request AS pr " +
            "SET pr.status = 'REJECTED' " +
            "WHERE pr.status = 'PENDING' " +
            "AND pr.event = ?1 ")
    void rejectPendingRequests(Long eventId);
}