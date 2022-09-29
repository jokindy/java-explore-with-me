package ru.practicum.explorewithme.event.common;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explorewithme.event.Event;
import ru.practicum.explorewithme.event.EventClient;
import ru.practicum.explorewithme.event.dto.EventFullDto;
import ru.practicum.explorewithme.event.dto.EventShortDto;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("events")
@AllArgsConstructor
public class EventPublicController {

    private final EventClient client;
    private final EventPublicService eventPublicService;

    @GetMapping
    public List<EventShortDto> getEvents(@RequestParam(required = false) String text,
                                         @RequestParam(required = false) Long[] categories,
                                         @RequestParam(required = false) Boolean paid,
                                         @RequestParam(required = false)
                                         @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeStart,
                                         @RequestParam(required = false)
                                         @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeEnd,
                                         @RequestParam(defaultValue = "false") boolean onlyAvailable,
                                         @RequestParam(required = false) EventSort sort,
                                         @Valid @PositiveOrZero @RequestParam(defaultValue = "0") int from,
                                         @Valid @Positive @RequestParam(defaultValue = "10") int size,
                                         HttpServletRequest request) {
        log.debug("Get events");
        client.addRequest(request);
        List<Event> events = eventPublicService.getEvents(text, categories, paid, rangeStart,
                rangeEnd, onlyAvailable);
        return eventPublicService.getSortedEvents(events, from, size, sort);
    }

    @GetMapping("/{eventId}")
    public EventFullDto getEventById(@PathVariable long eventId, HttpServletRequest request) {
        log.debug("Get event id: {}", eventId);
        client.addRequest(request);
        return eventPublicService.getEventFullDto(eventId);
    }
}