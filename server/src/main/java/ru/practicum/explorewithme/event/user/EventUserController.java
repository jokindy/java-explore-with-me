package ru.practicum.explorewithme.event.user;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explorewithme.event.Event;
import ru.practicum.explorewithme.event.dto.*;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/users")
public class EventUserController {

    private final EventMapper eventMapper;
    private final EventUserService eventService;

    @GetMapping("/{userId}/events")
    public List<EventShortDto> getEventsByUser(@PathVariable long userId,
                                               @Valid @Positive @RequestParam(defaultValue = "0") int from,
                                               @Valid @Positive @RequestParam(defaultValue = "10") int size) {
        log.debug("EVENT USER CONTROLLER - get user id: {}'s events", userId);
        List<Event> events = eventService.getUserEvents(userId, from, size).getContent();
        return events.stream()
                .map(eventMapper::mapToShortDto)
                .collect(Collectors.toList());
    }

    @PostMapping("/{userId}/events")
    public EventFullDto postEventByUser(@Valid @RequestBody NewEventDto eventDto, @PathVariable long userId) {
        log.debug("EVENT USER CONTROLLER - post event by user id: {}, DTO - {}", userId, eventDto);
        Event event = eventMapper.map(eventDto, userId);
        eventService.save(event);
        return eventMapper.mapToFullDto(event);
    }

    @PatchMapping("/{userId}/events")
    public EventFullDto patchEventsByUser(@Valid @RequestBody UpdateEventRequest eventDto, @PathVariable long userId) {
        log.debug("EVENT USER CONTROLLER - patch event id: {} by user id: {}", eventDto.getEventId(), userId);
        Event event = eventService.getEventById(eventDto.getEventId());
        eventMapper.map(eventDto, event);
        eventService.patchEvent(event, userId);
        return eventMapper.mapToFullDto(event);
    }

    @GetMapping("/{userId}/events/{eventId}")
    public EventFullDto getEventByEventId(@PathVariable long userId, @PathVariable long eventId) {
        log.debug("EVENT USER CONTROLLER - get event id: {} by owner id: {}", eventId, userId);
        Event event = eventService.getEventByOwnerId(userId, eventId);
        return eventMapper.mapToFullDto(event);
    }

    @PatchMapping("/{userId}/events/{eventId}")
    public EventFullDto cancelEventByEventId(@PathVariable long userId, @PathVariable long eventId) {
        log.debug("EVENT USER CONTROLLER - cancel event id: {} by owner id: {}", eventId, userId);
        Event event = eventService.cancelEvent(userId, eventId);
        return eventMapper.mapToFullDto(event);
    }
}