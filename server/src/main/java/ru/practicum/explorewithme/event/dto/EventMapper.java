package ru.practicum.explorewithme.event.dto;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.stereotype.Component;
import ru.practicum.explorewithme.event.Event;
import ru.practicum.explorewithme.event.EventClient;
import ru.practicum.explorewithme.event.EventState;
import ru.practicum.explorewithme.request.RequestRepository;

import java.time.LocalDateTime;

@Component
public class EventMapper extends ModelMapper {

    private final RequestRepository requestRepository;
    private final EventClient eventClient;

    public EventMapper(RequestRepository requestRepository, EventClient eventClient) {
        this.requestRepository = requestRepository;
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


    public EventFullDto mapToFullDto(Event event) {
        EventFullDto eventDto = super.map(event, EventFullDto.class);
        int confirmedRequests = requestRepository.getConfirmedRequests(event.getId());
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
