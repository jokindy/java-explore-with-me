package ru.practicum.explorewithme.event.user;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explorewithme.event.Event;
import ru.practicum.explorewithme.event.dto.*;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/users")
public class EventUserController {

    private final EventMapper eventMapper;
    private final EventUserService eventService;

    @GetMapping("/{userId}/events")
    public List<EventShortDto> getEventsByUser(@PathVariable long userId,
                                               @Valid @PositiveOrZero @RequestParam(defaultValue = "0") int from,
                                               @Valid @Positive @RequestParam(defaultValue = "10") int size) {
        log.debug("Get user id: {}'s events", userId);
        return eventService.getUserEvents(userId, from, size);
    }

    @PostMapping("/{userId}/events")
    public EventFullDto postEventByUser(@Valid @RequestBody NewEventDto eventDto, @PathVariable long userId) {
        log.debug("Post event by user id: {}, DTO - {}", userId, eventDto);
        return eventService.save(eventDto, userId);
    }

    @PatchMapping("/{userId}/events")
    public EventFullDto patchEventsByUser(@Valid @RequestBody UpdateEventRequest eventDto, @PathVariable long userId) {
        log.debug("Patch event id: {} by user id: {}", eventDto.getEventId(), userId);
        Event event = eventService.getEventById(eventDto.getEventId());
        eventMapper.map(eventDto, event);
        eventService.patchEvent(event, userId);
        return eventMapper.mapToFullDto(event);
    }

    @GetMapping("/{userId}/events/{eventId}")
    public EventFullDto getEventByEventId(@PathVariable long userId, @PathVariable long eventId) {
        log.debug("Get event id: {} by owner id: {}", eventId, userId);
        return eventService.getEventByOwnerId(userId, eventId);
    }

    @PatchMapping("/{userId}/events/{eventId}")
    public EventFullDto cancelEventByEventId(@PathVariable long userId, @PathVariable long eventId) {
        log.debug("Cancel event id: {} by owner id: {}", eventId, userId);
        return eventService.cancelEvent(userId, eventId);
    }
}