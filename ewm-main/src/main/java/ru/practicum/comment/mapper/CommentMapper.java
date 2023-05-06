package ru.practicum.comment.mapper;

import org.springframework.stereotype.Service;
import ru.practicum.comment.dto.CommentFullDto;
import ru.practicum.comment.dto.CommentShortDto;
import ru.practicum.comment.model.Comment;

@Service
public class CommentMapper {

    public CommentFullDto toCommentFullDto(Comment comment) {
        CommentFullDto commentFullDto = new CommentFullDto();
        commentFullDto.setId(comment.getId());
        commentFullDto.setText(comment.getText());
        commentFullDto.setEvent(comment.getEvent());
        commentFullDto.setAuthor(comment.getAuthor());
        commentFullDto.setStatus(comment.getStatus());
        commentFullDto.setCreated(comment.getCreated());
        return commentFullDto;
    }

    public CommentShortDto toCommentShortDto(Comment comment) {
        CommentShortDto commentShortDto = new CommentShortDto();
        commentShortDto.setId(comment.getId());
        commentShortDto.setText(comment.getText());
        commentShortDto.setEvent(comment.getEvent());
        commentShortDto.setAuthor(comment.getAuthor());
        commentShortDto.setCreated(comment.getCreated());
        return commentShortDto;
    }
}
