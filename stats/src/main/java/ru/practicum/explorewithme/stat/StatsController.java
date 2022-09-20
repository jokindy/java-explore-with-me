package ru.practicum.explorewithme.stat;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explorewithme.stat.dto.EndpointHitDto;
import ru.practicum.explorewithme.stat.dto.ViewStats;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RestController
@AllArgsConstructor
public class StatsController {

    private final StatsService statsService;
    private final ModelMapper modelMapper;

    @GetMapping("/hit")
    public Integer getViews(@RequestParam String uri) {
        log.debug("STATS CONTROLLER - get views to event uri: {}", uri);
        return statsService.getViews(uri);
    }

    @PostMapping("/hit")
    public void save(@RequestBody EndpointHitDto endpointHitDto) {
        log.debug("STATS CONTROLLER - save new event request by uri: {}", endpointHitDto.getUri());
        EndpointHit endpointHit = modelMapper.map(endpointHitDto, EndpointHit.class);
        statsService.save(endpointHit);
    }

    @GetMapping("/stats")
    public List<ViewStats> getStats(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
                                    @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end,
                                    @RequestParam(required = false) String[] uris,
                                    @RequestParam(defaultValue = "false") boolean unique) {
        log.debug("STATS CONTROLLER - get views to event uri");
        return statsService.getStats(start, end, uris, unique);
    }
}