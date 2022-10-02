package ru.practicum.explorewithme.comment.user;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.explorewithme.comment.Comment;
import ru.practicum.explorewithme.comment.CommentRepository;
import ru.practicum.explorewithme.comment.CommentState;
import ru.practicum.explorewithme.comment.common.CommentPublicService;
import ru.practicum.explorewithme.comment.dto.UpdateCommentDto;
import ru.practicum.explorewithme.event.Event;
import ru.practicum.explorewithme.event.EventState;
import ru.practicum.explorewithme.event.user.EventUserService;
import ru.practicum.explorewithme.exception.CommentingIsForbiddenException;
import ru.practicum.explorewithme.exception.UpdateIsForbiddenException;
import ru.practicum.explorewithme.request.Request;
import ru.practicum.explorewithme.request.RequestStatus;
import ru.practicum.explorewithme.request.RequestUserService;
import ru.practicum.explorewithme.user.UserAdminService;
import ru.practicum.explorewithme.util.Mapper;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class CommentUserService {

    private final Mapper mapper;
    private final EntityManager entityManager;
    private final UserAdminService userService;
    private final EventUserService eventService;
    private final RequestUserService requestUserService;
    private final CommentRepository commentRepository;
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
    public Comment update(UpdateCommentDto dto, long userId, long eventId) {
        log.debug("Updating comment id: {} by user id: {}", dto.getId(), userId);
        Comment comment = new Comment();
        mapper.map(commentPublicService.getComment(dto.getId()), comment);
        mapper.map(dto, comment);
        checkIds(userId, eventId);
        long commentId = comment.getId();
        Comment commentDb = commentPublicService.getComment(commentId);
        if (!commentDb.getState().equals(CommentState.PENDING)) {
            log.error("UpdateIsForbiddenException");
            throw new UpdateIsForbiddenException(String.format("Comment id: %s can't be update because it's published",
                    commentId));
        }
        if (commentDb.equals(comment)) {
            log.error("UpdateIsForbiddenException");
            throw new UpdateIsForbiddenException(String.format("Comment id: %s is the same", commentId));
        }
        return commentRepository.save(comment);
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

    private void checkIds(long userId, long eventId) {
        log.debug("Checking event id: {} and user id: {}", eventId, userId);
        userService.checkUserId(userId);
        Event event = eventService.getEventById(eventId);
        long initiatorId = event.getInitiatorId();
        if (initiatorId == userId) {
            log.error("CommentingIsForbiddenException");
            throw new CommentingIsForbiddenException("You can't leave comment on your event");
        }
        if (event.getState().equals(EventState.PENDING)) {
            log.error("CommentingIsForbiddenException");
            throw new CommentingIsForbiddenException("You can't leave comment on unpublished event");
        }
        if (event.getEventDate().isAfter(LocalDateTime.now())) {
            log.error("CommentingIsForbiddenException");
            throw new CommentingIsForbiddenException("You can't leave comment on future event");
        }
        Request request = requestUserService.getRequest(userId, eventId);
        if (request == null) {
            log.error("CommentingIsForbiddenException");
            throw new CommentingIsForbiddenException("You can't leave comment on event because you aren't participant");
        }
        if (request.getStatus() != RequestStatus.CONFIRMED) {
            log.error("CommentingIsForbiddenException");
            throw new CommentingIsForbiddenException("You can't leave comment on event because you aren't confirmed to event");
        }
    }
}
