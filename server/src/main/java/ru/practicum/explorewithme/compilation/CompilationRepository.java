package ru.practicum.explorewithme.compilation;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import ru.practicum.explorewithme.compilation.model.Compilation;

import java.util.Collection;

@Repository
public interface CompilationRepository extends JpaRepository<Compilation, Long> {
    Collection<Compilation> findByPinned(Boolean pinned, PageRequest of);

    @Modifying
    @Query(value = "delete from event_compilations where compilation_id = ?1 and event_id = ?2", nativeQuery = true)
    void deleteEventFromCompilation(Long compId, Long eventId);

    @Modifying
    @Query(value = "insert into event_compilations values (?1, ?2)", nativeQuery = true)
    void addEventToCompilation(Long compId, Long eventId);

    @Modifying
    @Query(value = "update Compilation c set c.pinned = ?1 where c.id = ?2")
    void setCompilationPinned(boolean pinned, Long compId);
}