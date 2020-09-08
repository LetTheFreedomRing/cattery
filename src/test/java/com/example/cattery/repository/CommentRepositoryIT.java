package com.example.cattery.repository;

import com.example.cattery.model.Cat;
import com.example.cattery.model.Comment;
import com.example.cattery.model.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ExtendWith(SpringExtension.class)
class CommentRepositoryIT {

    private static final String COMMENT_MESSAGE = "Comment";
    private static final LocalDate COMMENT_DATE = LocalDate.now();

    @Autowired
    private CommentRepository commentRepository;

    @AfterEach
    void tearDown() {
        commentRepository.deleteAll();
    }

    private static Comment prepareComment() {
        Comment comment = new Comment();
        comment.setMessage(COMMENT_MESSAGE);
        comment.setDate(COMMENT_DATE);
        return comment;
    }

    @Test
    void save() {
        // given
        Comment comment = prepareComment();

        // when
        commentRepository.save(comment);

        // then
        assertEquals(1, commentRepository.count());

        Comment foundComment = commentRepository.findAll().iterator().next();

        assertNotNull(foundComment.getId());
        assertEquals(COMMENT_MESSAGE, foundComment.getMessage());
        assertEquals(COMMENT_DATE, foundComment.getDate());
    }

    @Test
    void findById() {
        // given
        Long savedId = commentRepository.save(prepareComment()).getId();

        // when
        Comment foundComment = commentRepository.findById(savedId).orElseThrow(NullPointerException::new);

        // then
        assertEquals(COMMENT_MESSAGE, foundComment.getMessage());
    }

    @Test
    void delete() {
        // given
        Comment savedUser = commentRepository.save(prepareComment());

        // when
        commentRepository.delete(savedUser);

        // then
        assertEquals(0, commentRepository.count());
    }

    @Test
    void deleteById() {
        // given
        Long savedId = commentRepository.save(prepareComment()).getId();

        // when
        commentRepository.deleteById(savedId);

        // then
        assertEquals(0, commentRepository.count());
    }

    @Test
    void findByCatId(@Autowired CatRepository catRepository) {
        // given
        Cat cat = new Cat();
        cat.setName("Cat");

        Cat savedCat = catRepository.save(cat);

        Comment comment = prepareComment();
        comment.setCat(savedCat);

        // when
        commentRepository.save(comment);

        // then
        Collection<Comment> foundComments = commentRepository.findByCatId(savedCat.getId());

        assertEquals(1, foundComments.size());
        assertEquals(COMMENT_MESSAGE, foundComments.iterator().next().getMessage());

        Collection<Comment> notFoundComments = commentRepository.findByCatId(100L);
        assertEquals(0, notFoundComments.size());
    }

    @Test
    void findByUserId(@Autowired UserRepository userRepository) {
        // given
        User user = new User();
        user.setName("User");

        User savedUser = userRepository.save(user);

        Comment comment = prepareComment();
        comment.setUser(savedUser);

        // when
        commentRepository.save(comment);

        // then
        Collection<Comment> foundComments = commentRepository.findByUserId(savedUser.getId());

        assertEquals(1, foundComments.size());
        assertEquals(COMMENT_MESSAGE, foundComments.iterator().next().getMessage());

        Collection<Comment> notFoundComments = commentRepository.findByUserId(100L);
        assertEquals(0, notFoundComments.size());

        userRepository.deleteAll();
    }

}