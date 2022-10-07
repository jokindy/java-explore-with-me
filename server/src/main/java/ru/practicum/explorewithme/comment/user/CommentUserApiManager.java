package ru.practicum.explorewithme.comment.user;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.explorewithme.comment.Comment;
import ru.practicum.explorewithme.comment.common.CommentPublicService;
import ru.practicum.explorewithme.comment.dto.CommentDto;
import ru.practicum.explorewithme.comment.dto.NewCommentDto;
import ru.practicum.explorewithme.comment.dto.UpdateCommentDto;
import ru.practicum.explorewithme.util.Mapper;

import java.util.List;

@Service
@AllArgsConstructor
public class CommentUserApiManager {

    private final Mapper mapper;
    private final CommentUserService commentUserService;
    private final CommentPublicService commentPublicService;

    public List<CommentDto> getEventComments(long userId, long eventId) {
        List<Comment> comments = commentUserService.getEventComments(userId, eventId);
        return mapper.mapList(comments, CommentDto.class);
    }

    public CommentDto postCommentToEvent(NewCommentDto commentDto, long userId, long eventId) {
        Comment comment = mapper.map(commentDto, Comment.class);
        comment.setEventId(eventId);
        comment.setAuthorId(userId);
        commentUserService.save(comment, userId, eventId);
        return mapper.map(comment, CommentDto.class);
    }

    public CommentDto updateComment(UpdateCommentDto dto, long userId, long eventId) {
        Comment comment = new Comment();
        mapper.map(commentPublicService.getComment(dto.getId()), comment);
        mapper.map(dto, comment);
        commentUserService.update(comment, userId, eventId);
        return mapper.map(comment, CommentDto.class);
    }

    public void deleteComment(long userId, long eventId, long commentId) {
        commentUserService.deleteComment(userId, eventId, commentId);
    }

    public void handleLike(long userId, long eventId, long commentId, boolean isLike) {
        commentUserService.handleLike(userId, eventId, commentId, isLike);
    }
}