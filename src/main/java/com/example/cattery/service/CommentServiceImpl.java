package com.example.cattery.service;

import com.example.cattery.converter.CommentDTOConverter;
import com.example.cattery.dto.CommentDTO;
import com.example.cattery.model.Comment;
import com.example.cattery.repository.CommentRepository;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final CommentDTOConverter commentDTOConverter;

    public CommentServiceImpl(CommentRepository commentRepository, CommentDTOConverter commentDTOConverter) {
        this.commentRepository = commentRepository;
        this.commentDTOConverter = commentDTOConverter;
    }

    @Override
    public Set<Comment> getByCatId(Long id) {
        return new HashSet<>(commentRepository.findByCatId(id));
    }

    @Override
    public Set<Comment> getByUserId(Long id) {
        return new HashSet<>(commentRepository.findByUserId(id));
    }

    @Override
    public Comment create(CommentDTO commentDTO) {
        return commentRepository.save(commentDTOConverter.convert(commentDTO));
    }
}
