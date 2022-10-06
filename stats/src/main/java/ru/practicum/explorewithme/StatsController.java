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
    public void saveHit(@Valid @RequestBody EndpointHitDto endpointHitDto) {
        log.info("StatsController: сохранение просмотра hit={}", endpointHitDto.getUri());
        EndpointHit endpointHit = StatsMapper.toModel(endpointHitDto);
        service.save(endpointHit);
    }

    @GetMapping("/stats") // Получение статистики по посещениям
    public List<ViewStats> getStats(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end,
            @RequestParam(required = false) String[] uris,
            @RequestParam(defaultValue = "false") boolean unique) {
        log.info("StatsController: получение статистики по посещениям за период с {} по {}.", start, end);
        return service.getStats(start, end, uris, unique);
    }

    @GetMapping("/hit") // Получение статистики по просмотрам
    public Integer getViews(@RequestParam String uri) {
        log.info("StatsController: получение статистики просмотров по uri={}.", uri);
        return service.getViews(uri);
    }
}