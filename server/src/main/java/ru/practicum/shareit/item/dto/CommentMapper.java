package ru.practicum.shareit.item.dto;

import lombok.experimental.UtilityClass;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.item.model.Item;

@UtilityClass
public class CommentMapper {

    public static CommentDto commentDto(Comment comment, User author, Item item) {
        CommentDto commentDto = new CommentDto();
        commentDto.setId(comment.getId());
        commentDto.setText(comment.getText());
        commentDto.setAuthor(author);
        commentDto.setItem(item);
        commentDto.setCreated(comment.getCreated());
        return commentDto;
    }

    public static CommentResponseDto commentDtoToResponseDto(CommentDto commentDto) {
        CommentResponseDto commentResponseDto = new CommentResponseDto();
        commentResponseDto.setId(commentDto.getId());
        commentResponseDto.setText(commentDto.getText());
        commentResponseDto.setAuthorName(commentDto.getAuthor().getName());
        commentResponseDto.setCreated(commentDto.getCreated());
        return commentResponseDto;
    }
}