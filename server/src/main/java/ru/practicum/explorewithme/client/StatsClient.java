package ru.practicum.explorewithme.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;

import javax.servlet.http.HttpServletRequest;

/**
 * HTTP client для запросов к сервису статистики
 */

@Service
public class StatsClient extends BaseClient {
    @Value("${app-name}")
    private String appName;

    @Autowired
    public StatsClient(@Value("${stats-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl))
                        .build()
        );
    }

    public void save(HttpServletRequest request) {
        String uri = request.getRequestURI();
        String ip = request.getRemoteAddr();
        post("/hit", new EndpointHit(appName, uri, ip));
    }

    public Object getViews(String uri) {
        return get("/hit?uri=" + uri).getBody();
    }
}