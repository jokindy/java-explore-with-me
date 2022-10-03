package ru.practicum.explorewithme.comment;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findAllByEventIdAndStateAndPositive(long eventId, CommentState state,
                                                      Boolean positive, Pageable pageable);

    @Modifying
    @Query(value = "update Comment c set c.state = ?1 where c.id = ?2")
    void updateCommentStatus(CommentState state, long commentId);

    @Modifying
    @Query(value = "insert into comment_rating values (?1, ?2, ?3)", nativeQuery = true)
    void rateComment(long commentId, long userId, boolean isLike);

}
