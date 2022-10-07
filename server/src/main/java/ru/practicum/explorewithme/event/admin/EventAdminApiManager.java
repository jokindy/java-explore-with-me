package ru.practicum.explorewithme.event.admin;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import ru.practicum.explorewithme.event.Event;
import ru.practicum.explorewithme.event.EventClient;
import ru.practicum.explorewithme.event.EventState;
import ru.practicum.explorewithme.event.dto.AdminUpdateEventDto;
import ru.practicum.explorewithme.event.dto.EventFullDto;
import ru.practicum.explorewithme.event.dto.EventMapper;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class EventAdminApiManager {

    private final EventMapper eventMapper;
    private final EventClient eventClient;
    private final EventAdminService eventAdminService;

    @GetMapping
    public List<EventFullDto> getEvents(Long[] users,
                                        EventState[] states,
                                        Long[] categories,
                                        LocalDateTime rangeStart,
                                        LocalDateTime rangeEnd,
                                        int from,
                                        int size) {
        List<Event> events = eventAdminService.getEvents(users, states, categories, rangeStart, rangeEnd);
        return events.stream()
                .map(event -> eventMapper.mapToFullDto(event, getViews(event.getId())))
                .skip((long) from * size)
                .limit(size)
                .collect(Collectors.toList());
    }

    public EventFullDto putEvent(AdminUpdateEventDto eventDto, long eventId) {
        Event event = eventAdminService.putEvent(eventDto, eventId);
        return eventMapper.mapToFullDto(event, getViews(eventId));
    }

    public EventFullDto handleEventState(long eventId, boolean isPublish) {
        Event event = eventAdminService.changeEventState(eventId, isPublish);
        return eventMapper.mapToFullDto(event, getViews(eventId));

    }

    private int getViews(long eventId) {
        String uri = "/events/" + eventId;
        return (int) eventClient.getViews(uri);
    }
}
