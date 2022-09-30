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
import ru.practicum.explorewithme.exception.ModelNotFoundException;
import ru.practicum.explorewithme.util.PageMaker;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class CommentPublicService {

    private final CommentRepository commentRepository;
    private final PageMaker<Comment> pageMaker;

    public List<Comment> getComments(long eventId, int from, int size, CommentSort sort, Boolean positive) {
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
        List<Comment> comments = commentRepository.findAllByEventIdAndState(eventId, CommentState.PUBLISHED, pageable);
        if (positive == null) {
            return pageMaker.getPage(comments, pageable).getContent();
        } else {
            return pageMaker.getPage(comments, pageable).getContent().stream()
                    .filter(comment -> comment.isPositive() == positive)
                    .collect(Collectors.toList());
        }
    }

    public Comment getCommentByEventId(long eventId, long commentId) {
        Comment comment = getComment(commentId);
        if (comment.getEventId() != eventId) {
            throw new CommentingIsForbiddenException(String.format("Comment id: %s isn't for event id: %s",
                    commentId, eventId));
        }
        return comment;
    }

    public Comment getComment(long commentId) {
        Optional<Comment> optionalComment = commentRepository.findById(commentId);
        if (optionalComment.isEmpty()) {
            log.error("ModelNotFoundException");
            throw new ModelNotFoundException(commentId, Comment.class.getSimpleName());
        }
        return optionalComment.get();
    }

    @Transactional
    public void handleUseful(long eventId, long commentId, boolean isAdding) {
        Comment comment = getCommentByEventId(eventId, commentId);
        int useful = comment.getUseful();
        if (isAdding) {
            useful++;
            commentRepository.updateUseful(useful, commentId);
        } else {
            useful--;
            commentRepository.updateUseful(useful, commentId);
        }
    }
}
