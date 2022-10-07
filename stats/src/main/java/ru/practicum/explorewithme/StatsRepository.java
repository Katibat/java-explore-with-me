package ru.practicum.explorewithme;

import org.springframework.data.jpa.repository.*;
import ru.practicum.explorewithme.model.*;

import java.time.LocalDateTime;
import java.util.List;

public interface StatsRepository extends JpaRepository<EndpointHit, Long> {

    String QUERY_NON_UNIQUE = "select distinct(e.uri) as uri, " +
            "count(e.app) as hits, e.app as app " +
            "from EndpointHit e " +
            "where e.timestamp > ?1 " +
            "and e.timestamp < ?2 " +
            "group by e.app, (e.uri)";
    String QUERY_UNIQUE = QUERY_NON_UNIQUE + ", e.ip";

    @Query(value = "select count(id) from stats where uri = ?1", nativeQuery = true)
    Integer getViews(String uri);

    @Query(value = QUERY_NON_UNIQUE)
    List<ViewStats> findAllNotUnique(LocalDateTime start, LocalDateTime end);

    @Query(value = QUERY_UNIQUE)
    List<ViewStats> findAllUnique(LocalDateTime start, LocalDateTime end);
}