package ru.practicum.explorewithme.request;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.explorewithme.event.Event;
import ru.practicum.explorewithme.event.EventState;
import ru.practicum.explorewithme.event.user.EventUserService;
import ru.practicum.explorewithme.exception.ForbiddenException;
import ru.practicum.explorewithme.exception.ModelNotFoundException;
import ru.practicum.explorewithme.user.admin.UserAdminService;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@AllArgsConstructor
public class RequestUserService {

    private final RequestRepository requestRepository;
    private final UserAdminService userAdminService;
    private final EventUserService eventService;
    private final EntityManager entityManager;

    @Transactional
    public Request save(long userId, long eventId) {
        log.debug("Saving request by user id: {} to event id: {}", userId, eventId);
        Event event = eventService.getEventById(eventId);
        userAdminService.checkUserId(userId);
        if (event.getInitiatorId() == userId) {
            log.error("UserIsInitiatorException");
            throw ForbiddenException.userIsInitiator(userId, eventId);
        }
        if (!event.getState().equals(EventState.PUBLISHED)) {
            log.error("EventIsNotAvailableException");
            throw ForbiddenException.eventIsNotPublished(eventId);
        }
        int confirmedRequests = requestRepository.getConfirmedRequests(eventId);
        int participantLimit = event.getParticipantLimit();
        if (participantLimit != 0) {
            if (participantLimit <= confirmedRequests) {
                log.error("EventIsNotAvailableException");
                throw ForbiddenException.eventIsNotAvailable(eventId);
            }
        }
        Request request = event.isRequestModeration()
                ? new Request(userId, eventId, RequestStatus.PENDING)
                : new Request(userId, eventId, RequestStatus.CONFIRMED);
        requestRepository.save(request);
        entityManager.refresh(request);
        return request;
    }

    public List<Request> getRequests(long userId) {
        log.debug("Getting requests user id: {}", userId);
        userAdminService.checkUserId(userId);
        return requestRepository.findAllByRequesterId(userId);

    }

    @Transactional
    public Request cancelRequest(long userId, long reqId) {
        log.debug("Cancel request id: {} by user id: {}", reqId, userId);
        userAdminService.checkUserId(userId);
        Request request = getRequest(reqId);
        long requesterId = request.getRequesterId();
        if (request.getStatus().equals(RequestStatus.CANCELED)) {
            log.error("UpdateIsForbiddenException");
            throw ForbiddenException.cancelRequest(reqId);
        }
        if (requesterId != userId) {
            log.error("UserIsInitiatorException");
            throw ForbiddenException.userIsNotRequester(userId, reqId);
        }
        request.setStatus(RequestStatus.CANCELED);
        requestRepository.save(request);
        return request;
    }

    public Request getRequest(long reqId) {
        log.debug("Getting request id: {}", reqId);
        Optional<Request> optionalRequest = requestRepository.findById(reqId);
        if (optionalRequest.isEmpty()) {
            log.error("ModelNotFoundException");
            throw new ModelNotFoundException(reqId, Request.class.getSimpleName());
        }
        return optionalRequest.get();
    }

    public List<Request> getRequestsByEventId(long userId, long eventId) {
        log.debug("Getting event id: {}'s requests by user id: {}", eventId, userId);
        eventService.getEventByOwnerId(userId, eventId);
        return eventService.getEventById(eventId).getRequests();
    }

    public Request handleRequest(long userId, long eventId, long reqId, boolean isApproved) {
        log.debug("Change request id: {}'s status to event id: {} by user id: {}", reqId, eventId, userId);
        eventService.getEventByOwnerId(userId, eventId);
        List<Request> requests = eventService.getEventById(eventId).getRequests();
        Request request = getRequest(reqId);
        if (!requests.contains(request)) {
            log.error("UpdateIsForbiddenException");
            throw ForbiddenException.updateRequest(reqId, eventId);
        }
        if (!request.getStatus().equals(RequestStatus.PENDING)) {
            log.error("UpdateIsForbiddenException");
            throw ForbiddenException.updateUpdatedRequest(reqId);
        }
        if (isApproved) {
            request.setStatus(RequestStatus.CONFIRMED);
        } else {
            request.setStatus(RequestStatus.REJECTED);
        }
        requestRepository.save(request);
        return request;
    }

    public Request getRequest(long userId, long eventId) {
        return requestRepository.findByRequesterIdAndEventId(userId, eventId);
    }
}
