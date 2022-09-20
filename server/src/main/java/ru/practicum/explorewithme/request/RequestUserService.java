package ru.practicum.explorewithme.request;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.explorewithme.event.Event;
import ru.practicum.explorewithme.event.EventState;
import ru.practicum.explorewithme.event.user.EventUserService;
import ru.practicum.explorewithme.exception.*;
import ru.practicum.explorewithme.user.UserAdminService;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@AllArgsConstructor
public class RequestUserService {

    private final RequestRepo requestRepo;
    private final UserAdminService userAdminService;
    private final EventUserService eventService;

    public Request save(long userId, long eventId) {
        log.debug("REQUEST USER SERVICE - saving request by user id: {} to event id: {}", userId, eventId);
        Event event = eventService.getEventById(eventId);
        userAdminService.checkUserId(userId);
        if (event.getInitiatorId() == userId) {
            log.warn("REQUEST USER SERVICE - UserIsInitiatorException");
            throw new UserIsInitiatorException("You can't make request on your event");
        }
        if (!event.getState().equals(EventState.PUBLISHED)) {
            log.warn("REQUEST USER SERVICE - EventIsNotAvailableException");
            throw new EventIsNotAvailableException(String.format("You can't make request to event id: %s because " +
                    "this event is not published", eventId));
        }
        int confirmedRequests = requestRepo.getConfirmedRequests(eventId);
        int participantLimit = event.getParticipantLimit();
        if (participantLimit != 0) {
            if (participantLimit <= confirmedRequests) {
                log.warn("REQUEST USER SERVICE - EventIsNotAvailableException");
                throw new EventIsNotAvailableException(String.format("You can't make request to event id: %s because " +
                        "participant limit is over", eventId));
            }
        }
        Request request = event.isRequestModeration()
                ? new Request(userId, eventId, RequestStatus.PENDING)
                : new Request(userId, eventId, RequestStatus.CONFIRMED);
        return requestRepo.save(request);
    }

    public List<Request> getRequests(long userId) {
        log.debug("REQUEST USER SERVICE - getting requests user id: {}", userId);
        userAdminService.checkUserId(userId);
        return requestRepo.findAllByRequesterId(userId);
    }

    @Transactional
    public Request cancelRequest(long userId, long reqId) {
        log.debug("REQUEST USER SERVICE - cancel request id: {} by user id: {}", reqId, userId);
        userAdminService.checkUserId(userId);
        Request request = getRequest(reqId);
        long requesterId = request.getRequesterId();
        if (request.getStatus().equals(RequestStatus.CANCELED)) {
            log.warn("REQUEST USER SERVICE - UpdateIsForbiddenException");
            throw new UpdateIsForbiddenException(String.format("Request id: %s can't be updated because " +
                    "it's already canceled", reqId));
        }
        if (requesterId != userId) {
            log.warn("REQUEST USER SERVICE - UserNotInitiatorException");
            throw new UserNotInitiatorException("It's not your request");
        }
        request.setStatus(RequestStatus.CANCELED);
        return requestRepo.save(request);
    }

    public Request getRequest(long reqId) {
        log.debug("REQUEST USER SERVICE - getting request id: {}", reqId);
        Optional<Request> optionalRequest = requestRepo.findById(reqId);
        if (optionalRequest.isEmpty()) {
            log.warn("REQUEST USER SERVICE - ModelNotFoundException");
            throw new ModelNotFoundException(reqId, Request.class.getSimpleName());
        }
        return optionalRequest.get();
    }

    public List<Request> getRequestsByEventId(long userId, long eventId) {
        log.debug("REQUEST USER SERVICE - getting event id: {}'s requests by user id: {}", eventId, userId);
        Event event = eventService.getEventByOwnerId(userId, eventId);
        return event.getRequests();
    }

    public Request handleRequest(long userId, long eventId, long reqId, boolean isApproved) {
        log.debug("REQUEST USER SERVICE - change request id: {}'s status to event id: {} by user id: {}", reqId,
                eventId, userId);
        List<Request> requests = getRequestsByEventId(userId, eventId);
        Request request = getRequest(reqId);
        if (!requests.contains(request)) {
            log.warn("REQUEST USER SERVICE - UpdateIsForbiddenException");
            throw new UpdateIsForbiddenException(String.format("Request id: %s can't be updated because it don't " +
                    "related to event id: %s", reqId, eventId));
        }
        if (!request.getStatus().equals(RequestStatus.PENDING)) {
            log.warn("REQUEST USER SERVICE - UpdateIsForbiddenException");
            throw new UpdateIsForbiddenException(String.format("Request id: %s can't be updated because " +
                    "it already updated", reqId));
        }
        if (isApproved) {
            request.setStatus(RequestStatus.CONFIRMED);
        } else {
            request.setStatus(RequestStatus.REJECTED);
        }
        return requestRepo.save(request);
    }
}
