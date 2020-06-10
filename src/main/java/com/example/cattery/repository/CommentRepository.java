package com.example.cattery.repository;

import com.example.cattery.model.Comment;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Collection;

public interface CommentRepository extends CrudRepository<Comment, Long> {

    @Query("FROM Comment WHERE CAT_ID=?1")
    Collection<Comment> findByCatId(Long catId);

    @Query("FROM Comment WHERE USER_ID=?1")
    Collection<Comment> findByUserId(Long userId);
}
