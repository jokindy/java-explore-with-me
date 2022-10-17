package ru.practicum.explorewithme.comment.rating;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface CommentRatingRepository extends JpaRepository<CommentRating, Long> {

    @Query(value = "select cr from CommentRating cr where cr.commentId = ?1 and cr.userId = ?2")
    Optional<CommentRating> findByIds(long commentId, long userId);

    @Modifying
    @Query(value = "update CommentRating cr set cr.isLike = ?3 where cr.commentId = ?1 and cr.userId = ?2")
    void updateRate(long commentId, long userId, boolean isLike);
}
