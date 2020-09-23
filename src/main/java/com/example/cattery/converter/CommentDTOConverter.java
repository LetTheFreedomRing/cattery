package com.example.cattery.converter;

import com.example.cattery.dto.CommentDTO;
import com.example.cattery.model.Comment;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class CommentDTOConverter implements Converter<CommentDTO, Comment> {

    private final CatDTOConverter catDTOConverter;
    private final UserDTOConverter userDTOConverter;

    public CommentDTOConverter(CatDTOConverter catDTOConverter, UserDTOConverter userDTOConverter) {
        this.catDTOConverter = catDTOConverter;
        this.userDTOConverter = userDTOConverter;
    }

    @Override
    public Comment convert(CommentDTO commentDTO) {
        if (commentDTO == null) return null;
        Comment comment = new Comment();
        comment.setId(commentDTO.getId());
        comment.setDate(commentDTO.getDate());
        comment.setMessage(commentDTO.getMessage());
        comment.setCat(catDTOConverter.convert(commentDTO.getCat()));
        comment.setUser(userDTOConverter.convert(commentDTO.getUser()));
        return comment;
    }
}
