package ru.practicum.explorewithme.comment;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import ru.practicum.explorewithme.category.Category;
import ru.practicum.explorewithme.comment.user.CommentUserService;
import ru.practicum.explorewithme.event.Event;
import ru.practicum.explorewithme.exception.ForbiddenException;
import ru.practicum.explorewithme.request.Request;
import ru.practicum.explorewithme.user.User;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

@Transactional
@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@TestPropertySource(locations = "classpath:application-test.properties")
public class CommentUserServiceTest {

    private final EntityManager em;
    private final CommentUserService service;

    @Test
    public void contextLoad() {
        Assertions.assertNotNull(em);
    }

    @Test
    public void saveCommentByInitiator() {
        fillDatabase();
        Comment comment = CommentTestEntities.getComment(1, 1);
        ForbiddenException thrown = Assertions.assertThrows(ForbiddenException.class,
                () -> service.save(comment, comment.getAuthorId(), comment.getEventId()));
        Assertions.assertEquals("You can't leave comment on your event", thrown.getMessage());
    }

    @Test
    public void saveWithoutRequest() {
        fillDatabase();
        Comment comment = CommentTestEntities.getComment(2, 1);
        ForbiddenException thrown = Assertions.assertThrows(ForbiddenException.class,
                () -> service.save(comment, comment.getAuthorId(), comment.getEventId()));
        Assertions.assertEquals("You can't leave comment on event because you aren't participant",
                thrown.getMessage());
    }

    @Test
    public void save() {
        fillDatabase();
        Comment comment = CommentTestEntities.getComment(2, 1);
        Request request = CommentTestEntities.getRequest();
        em.persist(request);
        service.save(comment, comment.getAuthorId(), comment.getEventId());
        Assertions.assertNotNull(em.find(Comment.class, 1L));
    }

    @Test
    public void delete() {
        fillDatabase();
        Comment comment = CommentTestEntities.getComment(2, 1);
        Request request = CommentTestEntities.getRequest();
        em.persist(request);
        em.persist(comment);
        service.deleteComment(2, 1, 1);
        Assertions.assertNull(em.find(Comment.class, 1L));
    }

    private void fillDatabase() {
        User user = CommentTestEntities.getUser();
        User author = CommentTestEntities.getAuthor();
        Event event = CommentTestEntities.getEvent();
        Category category = CommentTestEntities.getCategory();
        em.persist(user);
        em.persist(author);
        em.persist(category);
        em.persist(event);
    }
}