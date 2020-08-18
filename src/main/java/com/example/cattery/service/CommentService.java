package com.example.cattery.service;

import com.example.cattery.model.Comment;

import java.util.Set;

public interface CommentService extends CrudService<Comment, Long> {

    Set<Comment> getByCatId(Long id);

    Set<Comment> getByUserId(Long id);

}
