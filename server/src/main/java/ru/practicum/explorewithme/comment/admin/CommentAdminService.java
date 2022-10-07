package ru.practicum.explorewithme.comment.admin;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.explorewithme.comment.Comment;
import ru.practicum.explorewithme.comment.CommentModerationStatus;
import ru.practicum.explorewithme.comment.CommentRepository;
import ru.practicum.explorewithme.comment.common.CommentPublicService;
import ru.practicum.explorewithme.exception.ForbiddenException;

import javax.transaction.Transactional;

import static ru.practicum.explorewithme.comment.CommentModerationStatus.*;

@Slf4j
@Service
@AllArgsConstructor
public class CommentAdminService {

    private final CommentPublicService commentPublicService;
    private final CommentRepository commentRepository;

    @Transactional
    public Comment handleCommentStatus(long commentId, boolean isPublished) {
        log.debug("Changing status for comment id: {}, is published {}", commentId, isPublished);
        Comment comment = commentPublicService.getComment(commentId);
        if (comment.getState() != PENDING) {
            throw ForbiddenException.updateCommentStatus(commentId);
        }
        CommentModerationStatus status = isPublished ? APPROVED : REJECTED;
        comment.setState(status);
        return commentRepository.save(comment);
    }

    public void deleteComment(long commentId) {
        log.debug("Deleting status for comment id: {}", commentId);
        Comment comment = commentPublicService.getComment(commentId);
        commentRepository.delete(comment);
    }
}