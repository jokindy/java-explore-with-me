package ru.practicum.explorewithme.event.dto;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.stereotype.Component;
import ru.practicum.explorewithme.event.Event;
import ru.practicum.explorewithme.event.EventClient;
import ru.practicum.explorewithme.event.EventState;
import ru.practicum.explorewithme.event.common.EventSort;
import ru.practicum.explorewithme.request.RequestRepo;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class EventMapper extends ModelMapper {

    private final RequestRepo requestRepo;
    private final EventClient eventClient;

    public EventMapper(RequestRepo requestRepo, EventClient eventClient) {
        this.requestRepo = requestRepo;
        this.eventClient = eventClient;
        setUp();
    }

    public Event map(NewEventDto eventDto, long userId) {
        Event event = super.map(eventDto, Event.class);
        event.setCategoryId(eventDto.getCategory());
        event.setInitiatorId(userId);
        event.setCreatedOn(LocalDateTime.now());
        event.setState(EventState.PENDING);
        return event;
    }

    public EventShortDto mapToShortDto(Event event) {
        EventShortDto eventDto = super.map(event, EventShortDto.class);
        int confirmedRequests = requestRepo.getConfirmedRequests(event.getId());
        eventDto.setConfirmedRequests(confirmedRequests);
        String uri = "/events/" + event.getId();
        Integer views = (Integer) eventClient.getViews(uri);
        eventDto.setViews(views);
        return eventDto;
    }

    public List<EventShortDto> getSortedList(List<Event> events, EventSort sort) {
        return events.stream()
                .map(this::mapToShortDto)
                .sorted((o1, o2) -> {
                    if (sort != null) {
                        switch (sort) {
                            case EVENT_DATE:
                                return o1.getEventDate().compareTo(o2.getEventDate());
                            case VIEWS:
                                return o1.getViews().compareTo(o2.getViews());
                            default:
                                return o1.getId().compareTo(o2.getId());
                        }
                    } else {
                        return o1.getId().compareTo(o2.getId());
                    }
                })
                .collect(Collectors.toList());
    }


    public EventFullDto mapToFullDto(Event event) {
        EventFullDto eventDto = super.map(event, EventFullDto.class);
        int confirmedRequests = requestRepo.getConfirmedRequests(event.getId());
        eventDto.setConfirmedRequests(confirmedRequests);
        String uri = "/events/" + event.getId();
        Integer views = (Integer) eventClient.getViews(uri);
        eventDto.setViews(views);
        return eventDto;
    }

    public void map(UpdateEventRequest eventRequest, Event event) {
        super.map(eventRequest, event);
        event.setId(eventRequest.getEventId());
        event.setCategoryId(eventRequest.getCategory());
    }

    private void setUp() {
        this.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
    }
}
