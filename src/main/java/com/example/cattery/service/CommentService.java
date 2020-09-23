package com.example.cattery.service;

import com.example.cattery.dto.CommentDTO;
import com.example.cattery.model.Comment;

import java.util.Set;

public interface CommentService {

    Set<Comment> getByCatId(Long id);

    Set<Comment> getByUserId(Long id);

    Comment create(CommentDTO commentDTO);

}
