package ru.practicum.explorewithme;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.explorewithme.model.*;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StatsService {
    private final StatsRepository repository;

    public void save(EndpointHit endpointHit) {
        log.info("StatsService: сохранение нового просмотра {}", endpointHit);
        repository.save(endpointHit);
    }

    public Integer getViews(String uri) {
        log.info("StatsService: получение статистики просмотров по uri={}.", uri);
        return repository.getViews(uri);
    }

    public List<ViewStats> getStats(LocalDateTime start, LocalDateTime end, String[] uris, boolean unique) {
        log.info("StatsService: получение статистики просмотров в период с {} по {} для uri={}, где unique={},"
                , start, end, uris, unique);
        List<ViewStats> viewStats;
        if (!unique) {
            viewStats = repository.findAllNotUnique(start, end);
        } else {
            viewStats = repository.findAllUnique(start, end);
        }
        if (uris != null) {
            return viewStats.stream()
                    .map(view -> filterByUri(view, uris))
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());
        } else {
            return viewStats;
        }
    }

    public ViewStats filterByUri(ViewStats viewStats, String[] uris) {
        for (String uri : uris) {
            if (viewStats.getUri().equals(uri)) {
                return viewStats;
            }
        }
        return null;
    }
}