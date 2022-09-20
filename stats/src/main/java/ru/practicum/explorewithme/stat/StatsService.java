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

    private final StatsRepo statsRepo;

    public void save(EndpointHit endpointHit) {
        log.debug("STATS SERVICE - saving new endpoint hit to DB: {}", endpointHit);
        statsRepo.save(endpointHit);
    }

    public Integer getViews(String uri) {
        log.debug("STATS SERVICE - getting views to uri: {}", uri);
        return statsRepo.getViews(uri);
    }

    public List<ViewStats> getStats(LocalDateTime start, LocalDateTime end, String[] uri, boolean unique) {
        log.debug("STATS SERVICE - getting stats by start: {}," +
                "end: {}," +
                "uri: {}," +
                "unique: {}", start, end, uri, unique);
        List<ViewStats> viewStats;
        if (!unique) {
            viewStats = statsRepo.findAllNotUnique(start, end);
        } else {
            viewStats = statsRepo.findAllUnique(start, end);
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
