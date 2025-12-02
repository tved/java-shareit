package ru.practicum.shareit.item.mapper;

import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.NewCommentRequest;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.Instant;

public class CommentMapper {
    public static Comment toComment(NewCommentRequest request, Item item, User author) {
        return Comment.builder()
                .text(request.getText())
                .author(author)
                .item(item)
                .createdAt(Instant.now())
                .build();
    }

    public static CommentDto toCommentDto(Comment comment) {
        return new CommentDto(
                comment.getId(),
                comment.getText(),
                comment.getItem().getId(),
                comment.getAuthor().getName(),
                comment.getCreatedAt()
        );
    }
}
