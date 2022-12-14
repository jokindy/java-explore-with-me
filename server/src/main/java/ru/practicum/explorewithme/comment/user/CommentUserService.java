package ru.practicum.explorewithme.comment.user;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.explorewithme.comment.Comment;
import ru.practicum.explorewithme.comment.CommentModerationStatus;
import ru.practicum.explorewithme.comment.CommentRepository;
import ru.practicum.explorewithme.comment.common.CommentPublicService;
import ru.practicum.explorewithme.comment.rating.CommentRatingService;
import ru.practicum.explorewithme.event.Event;
import ru.practicum.explorewithme.event.EventState;
import ru.practicum.explorewithme.event.user.EventUserService;
import ru.practicum.explorewithme.exception.ForbiddenException;
import ru.practicum.explorewithme.request.Request;
import ru.practicum.explorewithme.request.RequestStatus;
import ru.practicum.explorewithme.request.RequestUserService;
import ru.practicum.explorewithme.user.admin.UserAdminService;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class CommentUserService {

    private final EntityManager entityManager;
    private final UserAdminService userService;
    private final EventUserService eventService;
    private final RequestUserService requestUserService;
    private final CommentRepository commentRepository;
    private final CommentRatingService commentRatingService;
    private final CommentPublicService commentPublicService;

    @Transactional
    public Comment save(Comment comment, long userId, long eventId) {
        log.debug("Saving comment: {} to DB", comment);
        checkIds(userId, eventId);
        commentRepository.save(comment);
        entityManager.refresh(comment);
        return comment;
    }

    @Transactional
    public void update(Comment comment, long userId, long eventId) {
        log.debug("Updating comment id: {} by user id: {}", comment.getId(), userId);
        checkIds(userId, eventId);
        long commentId = comment.getId();
        Comment commentDb = commentPublicService.getComment(commentId);
        if (!commentDb.getState().equals(CommentModerationStatus.PENDING)) {
            log.error("UpdateIsForbiddenException");
            throw ForbiddenException.updateNotPublishedComment(commentId);
        }
        if (commentDb.equals(comment)) {
            log.error("UpdateIsForbiddenException");
            throw ForbiddenException.updateSameComment(commentId);
        }
        commentRepository.save(comment);
    }

    public List<Comment> getEventComments(long userId, long eventId) {
        log.debug("Getting comments to event id: {} by user id: {}", eventId, userId);
        Event event = eventService.getEventByOwnerId(userId, eventId);
        return event.getComments();
    }

    public void deleteComment(long userId, long eventId, long commentId) {
        log.debug("Deleting comment id: {} to event id: {} by user id: {}", commentId, eventId, userId);
        checkIds(userId, eventId);
        Comment comment = commentPublicService.getComment(commentId);
        commentRepository.delete(comment);
    }

    @Transactional
    public void handleLike(long userId, long eventId, long commentId, boolean isLike) {
        log.debug("Handle useful for event id: {}'s comment id: {}, is like - {}", eventId, commentId, isLike);
        Comment comment = commentPublicService.getCommentByEventId(eventId, commentId);
        if (comment.getAuthorId() == userId) {
            log.error("CommentingIsForbiddenException");
            throw ForbiddenException.rateOwnComment();
        }
        commentRatingService.saveRate(userId, commentId, isLike);
    }

    private void checkIds(long userId, long eventId) {
        log.debug("Checking event id: {} and user id: {}", eventId, userId);
        userService.checkUserId(userId);
        Event event = eventService.getEventById(eventId);
        long initiatorId = event.getInitiatorId();
        if (initiatorId == userId) {
            log.error("CommentingIsForbiddenException");
            throw ForbiddenException.commentForOwnEvent();
        }
        if (event.getState().equals(EventState.PENDING)) {
            log.error("CommentingIsForbiddenException");
            throw ForbiddenException.commentToUnpublishedEvent();
        }
        if (event.getEventDate().isAfter(LocalDateTime.now())) {
            log.error("CommentingIsForbiddenException");
            throw ForbiddenException.commentToFutureEvent();
        }
        Request request = requestUserService.getRequest(userId, eventId);
        if (request == null) {
            log.error("CommentingIsForbiddenException");
            throw ForbiddenException.commentByNotParticipant();
        }
        if (request.getStatus() != RequestStatus.CONFIRMED) {
            log.error("CommentingIsForbiddenException");
            throw ForbiddenException.commentByRejectedParticipant();
        }
    }
}