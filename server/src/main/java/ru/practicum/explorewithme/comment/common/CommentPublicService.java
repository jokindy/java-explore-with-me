package ru.practicum.explorewithme.comment.common;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.explorewithme.comment.Comment;
import ru.practicum.explorewithme.comment.CommentRepository;
import ru.practicum.explorewithme.comment.CommentState;
import ru.practicum.explorewithme.exception.CommentingIsForbiddenException;
import ru.practicum.explorewithme.exception.EntityIsNotAvailableException;
import ru.practicum.explorewithme.exception.ModelNotFoundException;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@AllArgsConstructor
public class CommentPublicService {

    private final CommentRepository commentRepository;

    public List<Comment> getComments(long eventId, int from, int size, CommentSort sort, Boolean positive) {
        log.debug("Getting event id: {}'s comments by {}, is positive - {}", eventId, sort, positive);
        Pageable pageable;
        switch (sort) {
            case DATE:
                pageable = PageRequest.of(from, size, Sort.by("created").descending());
                break;
            case USEFUL:
                pageable = PageRequest.of(from, size, Sort.by("useful").descending());
                break;
            default:
                pageable = PageRequest.of(from, size, Sort.by("id").descending());
        }
        return commentRepository.findAllByEventIdAndStateAndPositive(eventId, CommentState.PUBLISHED,
                positive, pageable);
    }

    public Comment getCommentByEventId(long eventId, long commentId) {
        log.debug("Getting event id: {}'s comment id: {}", eventId, commentId);
        Comment comment = getComment(commentId);
        if (comment.getEventId() != eventId) {
            throw new CommentingIsForbiddenException(String.format("Comment id: %s isn't for event id: %s",
                    commentId, eventId));
        }
        if (!comment.getState().equals(CommentState.PUBLISHED)) {
            throw new EntityIsNotAvailableException(String.format("Comment id: %s is not published yet", commentId));
        }
        return comment;
    }

    public Comment getComment(long commentId) {
        log.debug("Getting comment id: {}", commentId);
        Optional<Comment> optionalComment = commentRepository.findById(commentId);
        return optionalComment.orElseThrow(() -> {
            log.error("ModelNotFoundException");
            throw new ModelNotFoundException(commentId, Comment.class.getSimpleName());
        });
    }
}
