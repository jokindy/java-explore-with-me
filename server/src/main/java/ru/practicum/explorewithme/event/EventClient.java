package ru.practicum.explorewithme.event;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.explorewithme.util.BaseClient;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

@Slf4j
@Service
public class EventClient extends BaseClient {

    @Value("${app-name}")
    private String appName;

    @Autowired
    public EventClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl))
                        .build()
        );
    }

    public Object getViews(String uri) {
        return get("/hit?uri=" + uri).getBody();
    }

    public void addRequest(HttpServletRequest request) {
        String uri = request.getRequestURI();
        String ip = request.getRemoteAddr();
        log.debug("EVENT CLIENT - send {} to stats-server", uri);
        post("/hit", new EndpointHit(appName, uri, ip));
    }

    @Data
    static class EndpointHit {

        private String app;
        private String uri;
        private String ip;
        private LocalDateTime timestamp;

        public EndpointHit(String app, String uri, String ip) {
            this.app = app;
            this.uri = uri;
            this.ip = ip;
            this.timestamp = LocalDateTime.now();
        }
    }
}
