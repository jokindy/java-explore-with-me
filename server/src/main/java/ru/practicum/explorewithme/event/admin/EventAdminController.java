package ru.practicum.explorewithme.event.admin;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explorewithme.event.EventState;
import ru.practicum.explorewithme.event.dto.AdminUpdateEventDto;
import ru.practicum.explorewithme.event.dto.EventFullDto;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/admin/events")
@AllArgsConstructor
public class EventAdminController {

    private final EventAdminManager eventAdminManager;

    @GetMapping
    public List<EventFullDto> getEvents(@RequestParam(required = false) Long[] users,
                                        @RequestParam(required = false) EventState[] states,
                                        @RequestParam(required = false) Long[] categories,
                                        @RequestParam(required = false)
                                        @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeStart,
                                        @RequestParam(required = false)
                                        @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeEnd,
                                        @Valid @PositiveOrZero @RequestParam(defaultValue = "0") int from,
                                        @Valid @Positive @RequestParam(defaultValue = "10") int size) {
        log.debug("Get events");
        return eventAdminManager.getEvents(users, states, categories, rangeStart, rangeEnd, from, size);
    }

    @PutMapping("/{eventId}")
    public EventFullDto putEvent(@RequestBody AdminUpdateEventDto eventDto, @PathVariable long eventId) {
        log.debug("Update event id: {}, DTO - {}", eventId, eventDto);
        return eventAdminManager.putEvent(eventDto, eventId);
    }

    @PatchMapping("/{eventId}/publish")
    public EventFullDto publishEvent(@PathVariable long eventId) {
        log.debug("Publish event id: {}", eventId);
        return eventAdminManager.handleEventState(eventId, true);

    }

    @PatchMapping("/{eventId}/reject")
    public EventFullDto rejectEvent(@PathVariable long eventId) {
        log.debug("Reject event id: {}", eventId);
        return eventAdminManager.handleEventState(eventId, false);
    }
}