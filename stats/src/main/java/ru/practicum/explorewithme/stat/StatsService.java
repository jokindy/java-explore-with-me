package ru.practicum.explorewithme.stat;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.explorewithme.stat.dto.ViewStats;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class StatsService {

    private final StatsRepository statsRepository;

    public void save(EndpointHit endpointHit) {
        log.debug("Saving new endpoint hit to DB: {}", endpointHit);
        statsRepository.save(endpointHit);
    }

    public Integer getViews(String uri) {
        log.debug("Getting views to uri: {}", uri);
        return statsRepository.getViews(uri);
    }

    public List<ViewStats> getStats(LocalDateTime start, LocalDateTime end, String[] uri, boolean unique) {
        log.debug("Getting stats by start: {}," +
                "end: {}," +
                "uri: {}," +
                "unique: {}", start, end, uri, unique);
        List<ViewStats> viewStats;
        if (!unique) {
            viewStats = statsRepository.findAllNotUnique(start, end);
        } else {
            viewStats = statsRepository.findAllUnique(start, end);
        }
        if (uri != null) {
            return viewStats.stream()
                    .map(view -> filterByUri(view, uri))
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