package ru.practicum.explorewithme.comment.rating;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@ToString
@Entity
@Table(name = "comment_rating")
@NoArgsConstructor
public class CommentRating {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "comment_id")
    private Long commentId;

    @Column(name = "is_like")
    private boolean isLike;
}
