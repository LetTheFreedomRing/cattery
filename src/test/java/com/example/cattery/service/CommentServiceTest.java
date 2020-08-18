package com.example.cattery.service;

import com.example.cattery.exceptions.NotFoundException;
import com.example.cattery.model.Comment;
import com.example.cattery.repository.CommentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.Collections;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class CommentServiceTest {

    @Mock
    private CommentRepository commentRepository;

    @InjectMocks
    private CommentServiceImpl commentService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        commentService = new CommentServiceImpl(commentRepository);
    }

    @Test
    void getByCatId() {
        // given
        Comment comment = new Comment();
        Mockito.when(commentRepository.findByCatId(ArgumentMatchers.anyLong())).thenReturn(Collections.singletonList(comment));

        // when
        Set<Comment> comments = commentService.getByCatId(1L);

        // then
        assertEquals(1, comments.size());
        assertEquals(comment, comments.iterator().next());
    }

    @Test
    void getByUserId() {
        // given
        Comment comment = new Comment();
        Mockito.when(commentRepository.findByUserId(ArgumentMatchers.anyLong())).thenReturn(Collections.singletonList(comment));

        // when
        Set<Comment> comments = commentService.getByUserId(1L);

        // then
        assertEquals(1, comments.size());
        assertEquals(comment, comments.iterator().next());
    }

    @Test
    void getAll() {
        // given
        Comment comment = new Comment();
        Mockito.when(commentRepository.findAll()).thenReturn(Collections.singletonList(comment));

        // when
        Set<Comment> comments = commentService.getAll();

        // then
        assertEquals(1, comments.size());
        assertEquals(comment, comments.iterator().next());
    }

    @Test
    void getById() {
        // given
        Comment comment = new Comment();
        Mockito.when(commentRepository.findById(ArgumentMatchers.anyLong())).thenReturn(Optional.of(comment));

        // when
        Comment foundComment = commentService.getById(1L);

        // then
        assertEquals(comment, foundComment);
    }

    @Test
    void getByIdThrowsException() {
        // given
        Mockito.when(commentRepository.findById(ArgumentMatchers.anyLong())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> commentService.getById(1L));
    }

    @Test
    void create() {
        // given
        Comment comment = new Comment();
        comment.setId(1L);
        Mockito.when(commentRepository.save(ArgumentMatchers.any())).thenReturn(comment);

        // when
        Comment savedComment = commentService.create(new Comment());

        // then
        assertEquals(comment, savedComment);
    }
}