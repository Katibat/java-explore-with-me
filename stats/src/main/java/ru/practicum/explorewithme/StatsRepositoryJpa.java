//package ru.practicum.explorewithme;
//
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.jpa.repository.Query;
//
//public interface StatsRepositoryJpa extends JpaRepository<EndpointHit, Long> {
//    @Query(value = "select count(id) from statistics where uri = ?1", nativeQuery = true)
//    Integer getViews(String uri);
//}
