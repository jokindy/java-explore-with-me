package ru.practicum.explorewithme.comment.admin;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explorewithme.comment.dto.CommentDto;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/admin/comments")
public class CommentAdminController {

    private final CommentAdminManager commentAdminManager;

    /**
     * Метод меняет статус коммента на PUBLISHED
     */

    @PatchMapping("/{commentId}/publish")
    public CommentDto publishComment(@PathVariable long commentId) {
        log.debug("Publish comment id: {}", commentId);
       return commentAdminManager.handleCommentStatus(commentId, true);
    }

    /**
     * Метод меняет статус коммента на REJECTED
     */

    @PatchMapping("/{commentId}/reject")
    public CommentDto rejectComment(@PathVariable long commentId) {
        log.debug("Reject comment id: {}", commentId);
        return commentAdminManager.handleCommentStatus(commentId, false);
    }

    /**
     * Метод удаляет коммент (вдруг автор события посчитал его оскорбительным)
     */

    @DeleteMapping("/admin/comments/{commentId}")
    public String deleteComment(@PathVariable long commentId) {
        log.debug("Delete comment id: {}", commentId);
        commentAdminManager.deleteComment(commentId);
        return String.format("Comment id: %s is deleted", commentId);
    }
}
