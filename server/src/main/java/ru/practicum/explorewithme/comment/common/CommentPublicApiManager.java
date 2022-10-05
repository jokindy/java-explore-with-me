package ru.practicum.explorewithme.comment.common;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.explorewithme.comment.Comment;
import ru.practicum.explorewithme.comment.dto.CommentDto;
import ru.practicum.explorewithme.util.Mapper;

import java.util.List;

@Service
@AllArgsConstructor
public class CommentPublicApiManager {

    private final Mapper mapper;
    private final CommentPublicService commentPublicService;

    public List<CommentDto> getComments(long eventId,
                                        CommentSortKey sort,
                                        Boolean positive,
                                        int from,
                                        int size) {
        List<Comment> comments = commentPublicService.getComments(eventId, from, size, sort, positive);
        return mapper.mapList(comments, CommentDto.class);
    }

    public CommentDto getComment(long eventId, long commentId) {
        Comment comment = commentPublicService.getCommentByEventId(eventId, commentId);
        return mapper.map(comment, CommentDto.class);
    }
}
