package ru.practicum.explorewithme.comment.admin;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.explorewithme.comment.Comment;
import ru.practicum.explorewithme.comment.CommentRepository;
import ru.practicum.explorewithme.comment.common.CommentPublicService;
import ru.practicum.explorewithme.exception.UpdateIsForbiddenException;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import static ru.practicum.explorewithme.comment.CommentState.*;

@Slf4j
@Service
@AllArgsConstructor
public class CommentAdminService {

    private final CommentPublicService commentPublicService;
    private final CommentRepository commentRepository;
    private final EntityManager entityManager;

    @Transactional
    public Comment handleCommentStatus(long commentId, boolean isPublished) {
        log.debug("Changing status for comment id: {}, is published {}", commentId, isPublished);
        Comment comment = commentPublicService.getComment(commentId);
        if (comment.getState() != PENDING) {
            throw new UpdateIsForbiddenException(String.format("Comment id: %s can't be update because" +
                    " it's already updated", commentId));
        }
        if (isPublished) {
            commentRepository.updateCommentStatus(PUBLISHED, commentId);
        } else {
            commentRepository.updateCommentStatus(REJECTED, commentId);
        }
        entityManager.refresh(comment);
        return comment;
    }

    public void deleteComment(long commentId) {
        log.debug("Deleting status for comment id: {}", commentId);
        Comment comment = commentPublicService.getComment(commentId);
        commentRepository.delete(comment);
    }
}
