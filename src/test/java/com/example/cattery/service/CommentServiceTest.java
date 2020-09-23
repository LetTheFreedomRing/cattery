package com.example.cattery.service;

import com.example.cattery.converter.CommentDTOConverter;
import com.example.cattery.dto.CommentDTO;
import com.example.cattery.model.Comment;
import com.example.cattery.repository.CommentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.Collections;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class CommentServiceTest {

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private CommentDTOConverter commentDTOConverter;

    @InjectMocks
    private CommentServiceImpl commentService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        commentService = new CommentServiceImpl(commentRepository, commentDTOConverter);
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
    void create() {
        // given
        Comment comment = new Comment();
        comment.setId(1L);
        Mockito.when(commentRepository.save(ArgumentMatchers.any())).thenReturn(comment);

        // when
        Comment savedComment = commentService.create(new CommentDTO());

        // then
        assertEquals(comment, savedComment);
    }
}