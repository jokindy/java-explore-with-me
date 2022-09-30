package ru.practicum.explorewithme.comment.user;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.explorewithme.comment.Comment;
import ru.practicum.explorewithme.comment.CommentRepository;
import ru.practicum.explorewithme.comment.CommentState;
import ru.practicum.explorewithme.event.Event;
import ru.practicum.explorewithme.event.EventState;
import ru.practicum.explorewithme.event.user.EventUserService;
import ru.practicum.explorewithme.exception.ModelNotFoundException;
import ru.practicum.explorewithme.exception.CommentingIsForbiddenException;
import ru.practicum.explorewithme.exception.UpdateIsForbiddenException;
import ru.practicum.explorewithme.request.Request;
import ru.practicum.explorewithme.request.RequestStatus;
import ru.practicum.explorewithme.request.RequestUserService;
import ru.practicum.explorewithme.user.UserAdminService;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@AllArgsConstructor
public class CommentUserService {

    private final EntityManager entityManager;
    private final UserAdminService userService;
    private final EventUserService eventService;
    private final RequestUserService requestUserService;
    private final CommentRepository commentRepository;

    @Transactional
    public Comment save(Comment comment, long userId, long eventId) {
        checkIds(userId, eventId);
        commentRepository.save(comment);
        entityManager.refresh(comment);
        return comment;
    }

    @Transactional
    public void put(Comment comment, long userId, long eventId) {
        checkIds(userId, eventId);
        long commentId = comment.getId();
        Comment commentDb = getComment(commentId);
        if (!commentDb.getState().equals(CommentState.PENDING)) {
            log.error("UpdateIsForbiddenException");
            throw new UpdateIsForbiddenException(String.format("Comment id: %s can't be update because it's published",
                    commentId));
        }
        if (commentDb.equals(comment)) {
            log.error("UpdateIsForbiddenException");
            throw new UpdateIsForbiddenException(String.format("Comment id: %s is the same", commentId));
        }
        commentRepository.save(comment);
    }

    public List<Comment> getEventComments(long userId, long eventId) {
        Event event = eventService.getEventByOwnerId(userId, eventId);
        return event.getComments();
    }

    public void deleteComment(long userId, long eventId, long commentId) {
        checkIds(userId, eventId);
        Comment comment = getComment(commentId);
        commentRepository.delete(comment);
    }

    public Comment getComment(long commentId) {
        Optional<Comment> optionalComment = commentRepository.findById(commentId);
        if (optionalComment.isEmpty()) {
            log.error("ModelNotFoundException");
            throw new ModelNotFoundException(commentId, Comment.class.getSimpleName());
        }
        return optionalComment.get();
    }

    private void checkIds(long userId, long eventId) {
        userService.checkUserId(userId);
        Event event = eventService.getEventById(eventId);
        long initiatorId = event.getInitiatorId();
        if (initiatorId == userId) {
            throw new CommentingIsForbiddenException("You can't leave comment on your event");
        }
        if (event.getState().equals(EventState.PENDING)) {
            throw new CommentingIsForbiddenException("You can't leave comment on unpublished event");
        }
        if (event.getEventDate().isAfter(LocalDateTime.now())) {
            throw new CommentingIsForbiddenException("You can't leave comment on future event");
        }
        Request request = requestUserService.getRequest(userId, eventId);
        if (request == null) {
            throw new CommentingIsForbiddenException("You can't leave comment on event because you aren't participant");
        }
        if (request.getStatus() != RequestStatus.CONFIRMED) {
            throw new CommentingIsForbiddenException("You can't leave comment on event because you aren't confirmed to event");
        }
    }
}
