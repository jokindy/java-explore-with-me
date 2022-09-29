package ru.practicum.explorewithme.event.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.explorewithme.category.dto.CategoryDto;
import ru.practicum.explorewithme.event.Event;
import ru.practicum.explorewithme.event.EventState;
import ru.practicum.explorewithme.user.dto.UserShortDto;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventFullDto {

    private Long id;
    private String annotation;
    private String description;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdOn;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime eventDate;

    private LocationDto location;
    private long confirmedRequests;
    private CategoryDto category;
    private UserShortDto initiator;
    private boolean paid;
    private Integer participantLimit;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime publishedOn;

    private boolean requestModeration;
    private EventState state;
    private String title;
    private int views;

    public static EventFullDto construct(Event event, int views) {
        return new EventFullDto(event.getId(),
                event.getAnnotation(),
                event.getDescription(),
                event.getCreatedOn(),
                event.getEventDate(),
                LocationDto.construct(event.getLocation()),
                event.getConfirmedRequests(),
                CategoryDto.construct(event.getCategory()),
                UserShortDto.construct(event.getInitiator()),
                event.isPaid(),
                event.getParticipantLimit(),
                event.getPublishedOn(),
                event.isRequestModeration(),
                event.getState(),
                event.getTitle(),
                views);
    }
}
