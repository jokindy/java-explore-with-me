package ru.practicum.explorewithme.stat;

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
public class StatsController {

    private final StatsService statsService;
    private final ModelMapper modelMapper;

    public StatsController(StatsService statsService) {
        this.statsService = statsService;
        this.modelMapper = new ModelMapper();
    }

    @GetMapping("/hit")
    public Integer getViews(@RequestParam String uri) {
        log.debug("Get views to event uri: {}", uri);
        return statsService.getViews(uri);
    }

    @PostMapping("/hit")
    public void save(@RequestBody EndpointHitDto endpointHitDto) {
        log.debug("Save new event request by uri: {}", endpointHitDto.getUri());
        EndpointHit endpointHit = modelMapper.map(endpointHitDto, EndpointHit.class);
        statsService.save(endpointHit);
    }

    @GetMapping("/stats")
    public List<ViewStats> getStats(@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime start,
                                    @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime end,
                                    @RequestParam(required = false) String[] uris,
                                    @RequestParam(defaultValue = "false") boolean unique) {
        log.debug("Get views to event uri");
        return statsService.getStats(start, end, uris, unique);
    }
}
