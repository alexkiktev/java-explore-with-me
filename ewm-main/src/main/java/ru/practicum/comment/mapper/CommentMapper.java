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
        commentFullDto.setEvent(comment.getEvent().getTitle());
        commentFullDto.setAuthor(comment.getAuthor().getName());
        commentFullDto.setStatus(comment.getStatus());
        commentFullDto.setCreated(comment.getCreated());
        return commentFullDto;
    }

    public CommentShortDto toCommentShortDto(Comment comment) {
        CommentShortDto commentShortDto = new CommentShortDto();
        commentShortDto.setId(comment.getId());
        commentShortDto.setText(comment.getText());
        commentShortDto.setEvent(comment.getEvent().getTitle());
        commentShortDto.setAuthor(comment.getAuthor().getName());
        commentShortDto.setCreated(comment.getCreated());
        return commentShortDto;
    }
}
