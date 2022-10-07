package ru.practicum.explorewithme;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class StatsService {
    private final StatsRepository repository;
//    private final StatsRepositoryJpa repositoryJpa;

    @Transactional
    public EndpointHit save(EndpointHit endpointHit) {
        log.info("StatsService: сохранение нового просмотра {}", endpointHit);
        return repository.save(endpointHit);
    }

    public List<ViewStatsDto> getStats(List<String> uris,
                                  Boolean unique,
                                  LocalDateTime start,
                                  LocalDateTime end,
                                  String appName) {
        log.info("StatsService: получение статистики просмотров в период с {} по {} для uri={}, где unique={},"
                , start, end, uris, unique);
        List<ViewStatsDto> hits = repository.getStats(uris, unique, start, end, appName);
        hits.forEach(h -> h.setApp(appName));
        return hits;
    }

    public int getViews(String uri) {
        log.info("StatsService: получение статистики просмотров по uri={}.", uri);
        return repository.getViews(uri);
    }
}