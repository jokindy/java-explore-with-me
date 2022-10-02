package ru.practicum.explorewithme.comment.admin;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explorewithme.comment.Comment;
import ru.practicum.explorewithme.comment.dto.CommentDto;
import ru.practicum.explorewithme.util.Mapper;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/admin/comments")
public class CommentAdminController {

    private final Mapper mapper;
    private final CommentAdminService commentService;

    /**
     * Метод меняет статус коммента на PUBLISHED
     */

    @PatchMapping("/{commentId}/publish")
    public CommentDto publishComment(@PathVariable long commentId) {
        log.debug("Publish comment id: {}", commentId);
        Comment comment = commentService.handleCommentStatus(commentId, true);
        return mapper.map(comment, CommentDto.class);
    }

    /**
     * Метод меняет статус коммента на REJECTED
     */

    @PatchMapping("/{commentId}/reject")
    public CommentDto rejectComment(@PathVariable long commentId) {
        log.debug("Reject comment id: {}", commentId);
        Comment comment = commentService.handleCommentStatus(commentId, false);
        return mapper.map(comment, CommentDto.class);
    }

    /**
     * Метод удаляет коммент (вдруг автор события посчитал его оскорбительным)
     */

    @DeleteMapping("/admin/comments/{commentId}")
    public String deleteComment(@PathVariable long commentId) {
        log.debug("Delete comment id: {}", commentId);
        commentService.deleteComment(commentId);
        return String.format("Comment id: %s is deleted", commentId);
    }
}
