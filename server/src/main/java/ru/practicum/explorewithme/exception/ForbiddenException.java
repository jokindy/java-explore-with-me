package ru.practicum.explorewithme.exception;

import ru.practicum.explorewithme.event.EventState;

public class ForbiddenException extends RuntimeException {

    public ForbiddenException(String message) {
        super(message);
    }

    /**
     * Исключения для запросов
     */

    public static ForbiddenException eventIsNotPublished(long eventId) {
        return new ForbiddenException(String.format("You can't make request to event id: %s because " +
                "this event is not published", eventId));
    }

    public static ForbiddenException eventIsNotAvailable(long eventId) {
        return new ForbiddenException(String.format("You can't make request to event id: %s because " +
                "this event is not published", eventId));
    }

    public static ForbiddenException userIsNotInitiator(long userId, long eventId) {
        return new ForbiddenException(String.format("User id: %s is not initiator for event id: %s",
                userId, eventId));
    }

    public static ForbiddenException userIsNotRequester(long userId, long reqId) {
        return new ForbiddenException(String.format("User id: %s is not requester to request id: %s",
                userId, reqId));
    }

    public static ForbiddenException userIsInitiator(long userId, long eventId) {
        return new ForbiddenException(String.format("User id: %s is initiator for event id: %s",
                userId, eventId));
    }

    /**
     * Исключения для комментов
     */

    public static ForbiddenException commentIsNotAvailable(long commentId) {
        return new ForbiddenException(String.format("Comment id: %s is not published yet", commentId));
    }

    public static ForbiddenException commentForAnotherEvent(long commentId, long eventId) {
        return new ForbiddenException(String.format("Comment id: %s isn't for event id: %s",
                commentId, eventId));
    }

    public static ForbiddenException rateOwnComment() {
        return new ForbiddenException("You can't rate your comment");
    }

    public static ForbiddenException rateTwice(long commentId) {
        return new ForbiddenException(String.format("You already rated comment id: %s", commentId));
    }

    public static ForbiddenException commentForOwnEvent() {
        return new ForbiddenException("You can't leave comment on your event");
    }

    public static ForbiddenException commentToUnpublishedEvent() {
        return new ForbiddenException("You can't leave comment on unpublished event");
    }

    public static ForbiddenException commentToFutureEvent() {
        return new ForbiddenException("You can't leave comment on future event");
    }

    public static ForbiddenException commentByNotParticipant() {
        return new ForbiddenException("You can't leave comment on event because you aren't participant");
    }

    public static ForbiddenException commentByRejectedParticipant() {
        return new ForbiddenException("You can't leave comment on event because you aren't confirmed to event");
    }

    /**
     * Исключения для обновлений
     */

    public static ForbiddenException updateSameCategory(long catId) {
        return new ForbiddenException(String.format("Category id: %s is the same", catId));
    }

    public static ForbiddenException updateCommentStatus(long commentId) {
        return new ForbiddenException(String.format("Comment id: %s can't be update because" +
                " it's already updated", commentId));
    }

    public static ForbiddenException updateNotPublishedComment(long commentId) {
        return new ForbiddenException(String.format("Comment id: %s can't be update because it's published",
                commentId));
    }

    public static ForbiddenException updateSameComment(long commentId) {
        return new ForbiddenException(String.format("Comment id: %s is the same", commentId));
    }

    public static ForbiddenException updateUnpinnedCompilation(long compId) {
        return new ForbiddenException(String.format("Compilation id: %s is already unpinned", compId));
    }

    public static ForbiddenException updatePinnedCompilation(long compId) {
        return new ForbiddenException(String.format("Compilation id: %s is already pinned", compId));
    }

    public static ForbiddenException updateEventState(long eventId) {
        return new ForbiddenException(String.format("Event id: %s's state is already updated", eventId));
    }

    public static ForbiddenException updateEvent(long eventId, long userId) {
        return new ForbiddenException(String.format("Event id: %s can't be update because user id: %s is " +
                "not owner", eventId, userId));
    }

    public static ForbiddenException updatePublishedEvent(long eventId, EventState state) {
        return new ForbiddenException(String.format("Event id: %s can't be update because it status is: %s",
               eventId, state));
    }

    public static ForbiddenException cancelEvent(long eventId, EventState state) {
        return new ForbiddenException(String.format("Event id: %s can't be canceled because it status is: %s",
                eventId, state));
    }

    public static ForbiddenException cancelRequest(long reqId) {
        return new ForbiddenException(String.format("Request id: %s can't be updated because " +
                "it's already canceled", reqId));
    }

    public static ForbiddenException updateRequest(long reqId, long eventId) {
        return new ForbiddenException(String.format("Request id: %s can't be updated because it don't " +
                "related to event id: %s", reqId, eventId));
    }

    public static ForbiddenException updateUpdatedRequest(long reqId) {
        return new ForbiddenException(String.format("Request id: %s can't be updated because " +
                "it already updated", reqId));
    }
}
