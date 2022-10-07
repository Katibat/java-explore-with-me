package ru.practicum.explorewithme;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
public class StatsController {
    private final StatsService service;

    @PostMapping("/hit") // Сохранение информации о том, что к эндпоинту был запрос
    public EndpointHitDto saveHit(@Valid @RequestBody EndpointHitDto endpointHitDto) {
        log.info("StatsController: сохранение просмотра hit={}", endpointHitDto.getUri());
        EndpointHit endpointHit = StatsMapper.toModel(endpointHitDto);
        return StatsMapper.toHitDto(service.save(endpointHit));
    }

    @GetMapping("/stats") // Получение статистики по посещениям
    public List<ViewStatsDto> getStats(
            @RequestParam(name = "uris", required = false) List<String> uris,
            @RequestParam(name = "unique", defaultValue = "false") Boolean unique,
            @RequestParam(name = "start") LocalDateTime start,
            @RequestParam(name = "end") LocalDateTime end,
            @RequestParam(name = "app", defaultValue = "ewm-main-service") String appName) {
        log.info("StatsController: получение статистики по посещениям за период с {} по {}.", start, end);
        return service.getStats(uris, unique, start, end, appName);
    }

    @GetMapping("/hit") // Получение статистики по просмотрам
    public Integer getViews(@RequestParam String uri) {
        log.info("StatsController: получение статистики просмотров по uri={}.", uri);
        return service.getViews(uri);
    }
}