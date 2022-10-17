package ru.practicum.explorewithme.comment.rating;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import ru.practicum.explorewithme.exception.ForbiddenException;
import ru.practicum.explorewithme.exception.ModelNotFoundException;

import java.util.Optional;

@Slf4j
@Service
@AllArgsConstructor
public class CommentRatingService {

    private final CommentRatingRepository commentRatingRepository;

    public void saveRate(long userId, long commentId, boolean isLike) {
        log.debug("Saving rate for comment id: {} by user id: {}", commentId, userId);
        Optional<CommentRating> commentRatingO = commentRatingRepository.findByIds(commentId, userId);
        if (commentRatingO.isEmpty()) {
            CommentRating commentRating = new CommentRating();
            commentRating.setLike(isLike);
            commentRating.setUserId(userId);
            commentRating.setCommentId(commentId);
            try {
                commentRatingRepository.save(commentRating);
            } catch (DataIntegrityViolationException e) {
                log.error("CommentingIsForbiddenException");
                throw ForbiddenException.rateTwice(commentId);
            }
        } else {
            boolean like = commentRatingO.get().isLike();
            if (like == isLike) {
                log.error("CommentingIsForbiddenException");
                throw ForbiddenException.rateTwice(commentId);
            }
            commentRatingRepository.updateRate(commentId, userId, isLike);
        }
    }

    public void deleteRate(long userId, long commentId) {
        log.debug("Deleting rate for comment id: {} by user id: {}", commentId, userId);
        Optional<CommentRating> commentRatingO = commentRatingRepository.findByIds(commentId, userId);
        if (commentRatingO.isEmpty()) {
            log.error("ModelNotFoundException");
            throw new ModelNotFoundException(0L, CommentRating.class.getSimpleName());
        }
        commentRatingRepository.delete(commentRatingO.get());
    }
}
