package ru.practicum.explorewithme.comment;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import ru.practicum.explorewithme.category.Category;
import ru.practicum.explorewithme.event.Event;
import ru.practicum.explorewithme.user.User;

import java.util.Optional;

@DataJpaTest
@TestPropertySource(locations = "classpath:application-test.properties")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class CommentRepositoryTest {

    @Autowired
    private TestEntityManager em;

    @Autowired
    private CommentRepository commentRepository;

    @Test
    public void contextLoad() {
        Assertions.assertNotNull(em);
    }

    @Test
    public void testSaveComment() {
        fillDatabase();
        Optional<Comment> commentO = commentRepository.findById(1L);
        Assertions.assertNotNull(commentO);
        Assertions.assertEquals(CommentTestEntities.getAuthor().getEmail(), commentO.get().getAuthor().getEmail());
        Assertions.assertEquals(CommentTestEntities.getAuthor().getName(), commentO.get().getAuthor().getName());
        Assertions.assertEquals(CommentTestEntities.getEvent().getTitle(), commentO.get().getEvent().getTitle());
        Assertions.assertEquals(CommentTestEntities.getComment(2, 1).getContent(), commentO.get().getContent());
        Assertions.assertEquals(CommentTestEntities.getComment(2, 1).getState(), commentO.get().getState());
        Assertions.assertEquals(CommentTestEntities.getComment(2, 1).isPositive(), commentO.get().isPositive());
    }

    @Test
    public void testUpdateTestComment() {
        fillDatabase();
        commentRepository.updateCommentStatus(1L, CommentModerationStatus.APPROVED);
        Optional<Comment> commentO = commentRepository.findById(1L);
        em.refresh(commentO.get());
        Assertions.assertEquals(CommentModerationStatus.APPROVED, commentO.get().getState());
    }

    @Test
    public void testRateComment() {
        fillDatabase();
        commentRepository.rateComment(1L, 1L, true);
        commentRepository.rateComment(1L, 2L, true);
        Optional<Comment> commentO = commentRepository.findById(1L);
        em.refresh(commentO.get());
        int sum = commentO.get().getCommentRatings().stream()
                .mapToInt(rating -> rating.isLike() ? 1 : -1)
                .sum();
        Assertions.assertEquals(2, sum);
    }

    private void fillDatabase() {
        User user = CommentTestEntities.getUser();
        User author = CommentTestEntities.getAuthor();
        Event event = CommentTestEntities.getEvent();
        Category category = CommentTestEntities.getCategory();
        Comment comment = CommentTestEntities.getComment(2, 1);
        em.persist(user);
        em.persist(author);
        em.persist(category);
        em.persist(event);
        em.persist(comment);
        em.refresh(comment);
    }
}