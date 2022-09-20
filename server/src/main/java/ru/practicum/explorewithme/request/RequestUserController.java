package ru.practicum.explorewithme.request;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explorewithme.request.dto.ParticipantRequestDto;
import ru.practicum.explorewithme.request.dto.RequestMapper;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/users")
@AllArgsConstructor
public class RequestUserController {

    private final RequestUserService requestUserService;
    private final RequestMapper requestMapper;

    @GetMapping("/{userId}/requests")
    public List<ParticipantRequestDto> getRequestsByUser(@PathVariable long userId) {
        log.debug("REQUEST USER CONTROLLER - getting requests by user id: {}", userId);
        List<Request> requests = requestUserService.getRequests(userId);
        return requestMapper.mapList(requests);
    }

    @PostMapping("/{userId}/requests")
    public ParticipantRequestDto postRequestsByUser(@PathVariable long userId, @RequestParam long eventId) {
        log.debug("REQUEST USER CONTROLLER - post request by user id: {} to event id: {}", userId, eventId);
        Request request = requestUserService.save(userId, eventId);
        return requestMapper.map(request);
    }

    @PatchMapping("/{userId}/requests/{reqId}/cancel")
    public ParticipantRequestDto cancelRequestByUser(@PathVariable long userId, @PathVariable long reqId) {
        log.debug("REQUEST USER CONTROLLER - cancel request id: {} by user id: {}", reqId, userId);
        Request request = requestUserService.cancelRequest(userId, reqId);
        return requestMapper.map(request);
    }

    @GetMapping("/{userId}/events/{eventId}/requests")
    public List<ParticipantRequestDto> getRequestsByEventId(@PathVariable long userId, @PathVariable long eventId) {
        log.debug("REQUEST USER CONTROLLER - get event id: {}'s requests by user id: {}", eventId, userId);
        List<Request> list = requestUserService.getRequestsByEventId(userId, eventId);
        return requestMapper.mapList(list);
    }

    @PatchMapping("/{userId}/events/{eventId}/requests/{reqId}/confirm")
    public ParticipantRequestDto confirmRequestByEventId(@PathVariable long userId,
                                                         @PathVariable long eventId,
                                                         @PathVariable long reqId) {
        log.debug("REQUEST USER CONTROLLER - confirm request id: {} to event id: {} by user id: {}", reqId, eventId, userId);
        Request request = requestUserService.handleRequest(userId, eventId, reqId, true);
        return requestMapper.map(request);
    }

    @PatchMapping("/{userId}/events/{eventId}/requests/{reqId}/reject")
    public ParticipantRequestDto rejectRequestByEventId(@PathVariable long userId,
                                                        @PathVariable long eventId,
                                                        @PathVariable long reqId) {
        log.debug("REQUEST USER CONTROLLER - reject request id: {} to event id: {} by user id: {}", reqId, eventId, userId);
        Request request = requestUserService.handleRequest(userId, eventId, reqId, false);
        return requestMapper.map(request);
    }
}
