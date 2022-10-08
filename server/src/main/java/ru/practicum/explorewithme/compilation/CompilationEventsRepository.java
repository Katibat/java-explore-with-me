package ru.practicum.explorewithme.compilation;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.explorewithme.compilation.model.CompilationEvents;

import java.util.List;

@Repository
public interface CompilationEventsRepository extends JpaRepository<CompilationEvents, Long> {

    @Query("SELECT ce.compilation FROM CompilationEvents AS ce " +
            "WHERE ce.compilation = :compId")
    List<Long> findCompilationEventIds(Long compId);

    @Transactional
    void deleteByCompilationAndEvent(Long compId, Long eventId);
}
