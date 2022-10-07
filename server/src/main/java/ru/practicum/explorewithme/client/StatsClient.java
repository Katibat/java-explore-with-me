package ru.practicum.explorewithme.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

@Service
public class StatsClient extends BaseClient {
    private final static String APP = "explore-with-me-server";
    private final static String serverUrl = "http://localhost:9090";

    @Autowired
    public StatsClient(RestTemplateBuilder builder) {
        super(builder.uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl)).build());
    }

    public void saveHit(HttpServletRequest request) {
        String uri = request.getRequestURI();
        String ip = request.getRemoteAddr();
        post("/hit", new EndpointHit(APP, uri, ip, LocalDateTime.now()));
    }

    public Object getViews(String uri) {
        return get("/hit?uri=" + uri).getBody();
    }
}

//    public List<ViewStats> getViews(LocalDateTime start, LocalDateTime end, Set<String> uris, boolean unique) {
//        String requestUri = serverUrl + "/stats?start={start}&end={end}&uris={uris}&unique={unique}";
//        Map<String, String> urlParam = new HashMap<>();
//        urlParam.put("start", start.toString());
//        urlParam.put("end", end.toString());
//        urlParam.put("uris", String.join(",", uris));
//        urlParam.put("unique", Boolean.toString(unique));
//        ResponseEntity<ViewStats[]> entity = rest.getForEntity(requestUri, ViewStats[].class, urlParam);
//        return entity.getBody() != null ? Arrays.asList(entity.getBody()) : Collections.emptyList();
//    }

//    public void saveHit(HttpServletRequest request) {
//        String uri = request.getRequestURI();
//        String ip = request.getRemoteAddr();
//        HitDto hit = HitDto.builder().app(app).uri(uri).ip(ip).build();
//        HttpEntity<HitDto> entity = new HttpEntity<>(hit);
//        rest.postForObject(serverUrl + "/hit", entity, HitDto.class);
//    }