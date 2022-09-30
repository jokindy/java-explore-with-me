package ru.practicum.explorewithme.comment.admin;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.explorewithme.comment.Comment;
import ru.practicum.explorewithme.comment.dto.CommentDto;
import ru.practicum.explorewithme.util.Mapper;

@RestController
@AllArgsConstructor
public class CommentAdminController {

    private final Mapper mapper;
    private final CommentAdminService commentService;

    /**
     * Метод меняет статус коммента на PUBLISHED
     */

    @PatchMapping("/admin/comments/{commentId}/publish")
    public CommentDto publishComment(@PathVariable long commentId) {
        Comment comment = commentService.handleCommentStatus(commentId, true);
        return mapper.map(comment, CommentDto.class);
    }

    /**
     * Метод меняет статус коммента на REJECTED
     */

    @PatchMapping("/admin/comments/{commentId}/reject")
    public CommentDto rejectComment(@PathVariable long commentId) {
        Comment comment = commentService.handleCommentStatus(commentId, false);
        return mapper.map(comment, CommentDto.class);
    }

    /**
     * Метод удаляет коммент (вдруг автор события посчитал его оскорбительным)
     */

    @DeleteMapping("/admin/comments/{commentId}")
    public String deleteComment(@PathVariable long commentId) {
        commentService.deleteComment(commentId);
        return String.format("Comment id: %s is deleted", commentId);
    }
}
