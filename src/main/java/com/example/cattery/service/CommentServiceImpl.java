package com.example.cattery.service;

import com.example.cattery.exceptions.NotFoundException;
import com.example.cattery.model.Comment;
import com.example.cattery.repository.CommentRepository;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;

    public CommentServiceImpl(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
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
    public Set<Comment> getAll() {
        Set<Comment> comments = new HashSet<>();
        commentRepository.findAll().forEach(comments::add);
        return comments;
    }

    @Override
    public Comment getById(Long id) {
        return commentRepository.findById(id).orElseThrow(NotFoundException::new);
    }

    @Override
    public void delete(Comment comment) {
        commentRepository.delete(comment);
    }

    @Override
    public void deleteById(Long id) {
        commentRepository.deleteById(id);
    }

    @Override
    public Comment create(Comment comment) {
        return commentRepository.save(comment);
    }
}
