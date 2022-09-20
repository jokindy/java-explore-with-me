package ru.practicum.explorewithme.event;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.explorewithme.util.BaseClient;
import ru.practicum.explorewithme.util.EndpointHit;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@Service
public class EventClient extends BaseClient {

    @Value("${app-name}")
    private String appName;

    @Autowired
    public EventClient(@Value("${stats-server.url}") String serverUrl, RestTemplateBuilder builder) {
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
}
