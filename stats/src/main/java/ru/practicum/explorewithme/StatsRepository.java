package ru.practicum.explorewithme;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface StatsRepository extends JpaRepository<EndpointHit, Long> {

    @Query(value = "select count(id)" +
            " from statistics" +
            " where uri = ?1",
            nativeQuery = true)
    Integer getViews(String uri);

    String QUERY_NON_UNIQUE = "select distinct(eh.uri) as uri," +
            " count(eh.app) as hits, eh.app as app" +
            " from stats as st" +
            " where st.timestamp > ?1" +
            " and st.timestamp < ?2" +
            " group by st.app, (st.uri)";

    String QUERY_UNIQUE = QUERY_NON_UNIQUE + ", st.ip";

    @Query(value = QUERY_NON_UNIQUE)
    List<ViewStats> findAllNotUnique(LocalDateTime start, LocalDateTime end);

    @Query(value = QUERY_UNIQUE)
    List<ViewStats> findAllUnique(LocalDateTime start, LocalDateTime end);
}