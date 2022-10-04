package ru.practicum.explorewithme.request;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explorewithme.request.dto.ParticipantRequestDto;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/users")
@AllArgsConstructor
public class RequestUserController {

    private final RequestUserManager requestUserManager;

    @GetMapping("/{userId}/requests")
    public List<ParticipantRequestDto> getRequestsByUser(@PathVariable long userId) {
        log.debug("Getting requests by user id: {}", userId);
        return requestUserManager.getRequests(userId);
    }

    @PostMapping("/{userId}/requests")
    public ParticipantRequestDto postRequestsByUser(@PathVariable long userId, @RequestParam long eventId) {
        log.debug("Post request by user id: {} to event id: {}", userId, eventId);
        return requestUserManager.saveRequest(userId, eventId);
    }

    @PatchMapping("/{userId}/requests/{reqId}/cancel")
    public ParticipantRequestDto cancelRequestByUser(@PathVariable long userId, @PathVariable long reqId) {
        log.debug("Cancel request id: {} by user id: {}", reqId, userId);
        return requestUserManager.cancelRequest(userId, reqId);
    }

    @GetMapping("/{userId}/events/{eventId}/requests")
    public List<ParticipantRequestDto> getRequestsByEventId(@PathVariable long userId, @PathVariable long eventId) {
        log.debug("Get event id: {}'s requests by user id: {}", eventId, userId);
        return requestUserManager.getRequestsByEventId(userId, eventId);
    }

    @PatchMapping("/{userId}/events/{eventId}/requests/{reqId}/confirm")
    public ParticipantRequestDto confirmRequestByEventId(@PathVariable long userId,
                                                         @PathVariable long eventId,
                                                         @PathVariable long reqId) {
        log.debug("Confirm request id: {} to event id: {} by user id: {}", reqId, eventId, userId);
        return requestUserManager.handleRequestStatus(userId, eventId, reqId, true);
    }

    @PatchMapping("/{userId}/events/{eventId}/requests/{reqId}/reject")
    public ParticipantRequestDto rejectRequestByEventId(@PathVariable long userId,
                                                        @PathVariable long eventId,
                                                        @PathVariable long reqId) {
        log.debug("Reject request id: {} to event id: {} by user id: {}", reqId, eventId, userId);
        return requestUserManager.handleRequestStatus(userId, eventId, reqId, false);
    }
}