package ru.practicum.explorewithme.comment.admin;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.explorewithme.comment.Comment;
import ru.practicum.explorewithme.comment.dto.CommentDto;
import ru.practicum.explorewithme.util.Mapper;

@Service
@AllArgsConstructor
public class CommentAdminManager {

    private final Mapper mapper;
    private final CommentAdminService commentService;

    public CommentDto handleCommentStatus(long commentId, boolean isPublish) {
        Comment comment = commentService.handleCommentStatus(commentId, isPublish);
        return mapper.map(comment, CommentDto.class);
    }

    public void deleteComment(long commentId) {
        commentService.deleteComment(commentId);
    }
}
