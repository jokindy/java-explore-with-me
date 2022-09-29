package ru.practicum.explorewithme.request.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.explorewithme.request.Request;
import ru.practicum.explorewithme.request.RequestStatus;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ParticipantRequestDto {

    private Long id;
    private Long event;
    @JsonFormat(pattern = "yyyy.MM.dd HH:mm:ss")
    private LocalDateTime created;
    private Long requester;
    private RequestStatus status;

    public static ParticipantRequestDto construct(Request request) {
        return new ParticipantRequestDto(request.getId(),
                request.getEventId(),
                request.getCreated(),
                request.getRequesterId(),
                request.getStatus());
    }
}
