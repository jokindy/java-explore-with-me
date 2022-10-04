package ru.practicum.explorewithme.request;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.explorewithme.request.dto.ParticipantRequestDto;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class RequestUserManager {

    private final RequestUserService requestUserService;

    public ParticipantRequestDto saveRequest(long userId, long eventId) {
        Request request = requestUserService.save(userId, eventId);
        return ParticipantRequestDto.construct(request);
    }

    public List<ParticipantRequestDto> getRequests(long userId) {
        List<Request> requests = requestUserService.getRequests(userId);
        return requests.stream()
                .map(ParticipantRequestDto::construct)
                .collect(Collectors.toList());
    }

    public ParticipantRequestDto cancelRequest(long userId, long reqId) {
        Request request = requestUserService.cancelRequest(userId, reqId);
        return ParticipantRequestDto.construct(request);
    }

    public List<ParticipantRequestDto> getRequestsByEventId(long userId, long eventId) {
        List<Request> requests = requestUserService.getRequestsByEventId(userId, eventId);
        return requests.stream()
                .map(ParticipantRequestDto::construct)
                .collect(Collectors.toList());
    }

    public ParticipantRequestDto handleRequestStatus(long userId, long eventId, long reqId, boolean isConfirm) {
        Request request = requestUserService.handleRequest(userId, eventId, reqId, isConfirm);
        return ParticipantRequestDto.construct(request);
    }


}
