package ru.practicum.explorewithme;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class StatsService {
    private final StatsRepository repository;

    @Transactional
    public void save(EndpointHit endpointHit) {
        log.info("StatsService: сохранение нового просмотра {}", endpointHit);
        repository.save(endpointHit);
    }

    public List<ViewStats> getStats(LocalDateTime start, LocalDateTime end, String[] uris, boolean unique) {
        log.info("StatsService: получение статистики просмотров в период с {} по {} для uri={}, где unique={},"
                , start, end, uris, unique);
        List<ViewStats> viewStatsList;
        if (!unique) {
            viewStatsList = repository.findAllNotUnique(start, end);
        } else {
            viewStatsList = repository.findAllUnique(start, end);
        }
        if (uris != null) {
            return viewStatsList.stream()
                    .map(view -> filterByUri(view, uris))
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());
        } else {
            return viewStatsList;
        }
    }

    private ViewStats filterByUri(ViewStats viewStats, String[] uris) {
        for (String uri : uris) {
            if (viewStats.getUri().equals(uri)) {
                return viewStats;
            }
        }
        return null;
    }

    public int getViews(String uri) {
        log.info("StatsService: получение статистики просмотров по uri={}.", uri);
        return repository.getViews(uri);
    }
}