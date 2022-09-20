package ru.practicum.explorewithme.event.admin;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explorewithme.event.Event;
import ru.practicum.explorewithme.event.EventState;
import ru.practicum.explorewithme.event.dto.AdminUpdateEventDto;
import ru.practicum.explorewithme.event.dto.EventFullDto;
import ru.practicum.explorewithme.event.dto.EventMapper;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/admin/events")
@AllArgsConstructor
public class EventAdminController {

    private final EventAdminService eventAdminService;
    private final EventMapper eventMapper;

    @GetMapping
    public List<EventFullDto> getEvents(@RequestParam(required = false) Long[] users,
                                        @RequestParam(required = false) EventState[] states,
                                        @RequestParam(required = false) Long[] categories,
                                        @RequestParam(required = false)
                                        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime rangeStart,
                                        @RequestParam(required = false)
                                        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime rangeEnd,
                                        @RequestParam(defaultValue = "0") int from,
                                        @RequestParam(defaultValue = "10") int size) {
        log.debug("EVENT ADMIN CONTROLLER - get events");
        List<Event> events = eventAdminService.getEvents(users, states, categories, rangeStart, rangeEnd, from, size);
        return events.stream()
                .map(eventMapper::mapToFullDto)
                .collect(Collectors.toList());
    }

    @PutMapping("/{eventId}")
    public EventFullDto putEvent(@RequestBody AdminUpdateEventDto eventDto, @PathVariable long eventId) {
        log.debug("EVENT ADMIN CONTROLLER - update event id: {}, DTO - {}", eventId, eventDto);
        Event event = eventAdminService.getEvent(eventId);
        eventMapper.map(eventDto, event);
        eventAdminService.putEvent(event);
        return eventMapper.mapToFullDto(event);
    }

    @PatchMapping("/{eventId}/publish")
    public EventFullDto publishEvent(@PathVariable long eventId) {
        log.debug("EVENT ADMIN CONTROLLER - publish event id: {}", eventId);
        Event event = eventAdminService.changeEventState(eventId, true);
        return eventMapper.mapToFullDto(event);

    }

    @PatchMapping("/{eventId}/reject")
    public EventFullDto rejectEvent(@PathVariable long eventId) {
        log.debug("EVENT ADMIN CONTROLLER - reject event id: {}", eventId);
        Event event = eventAdminService.changeEventState(eventId, false);
        return eventMapper.mapToFullDto(event);
    }
}