package ru.practicum.comment.service;

import ru.practicum.comment.dto.*;
import ru.practicum.comment.model.ActionComment;
import ru.practicum.comment.model.StatusComment;

import java.time.LocalDateTime;
import java.util.List;

public interface CommentService {
    CommentFullDto createComment(Long userId, Long eventId, CommentNewDto commentNewDto);

    CommentFullDto updateComment(Long userId, Long commentId, CommentNewDto commentNewDto);

    void deleteComment(Long userId, Long commentId);

    List<CommentFullDto> getCommentsByUser(Long userId, StatusComment statusComments);

    CommentFullDto updateStatusComment(Long commentId, ActionComment actionComment);

    List<CommentFullDto> getCommentsAdmin(List<Long> events, List<Long> users, List<StatusComment> statusComments,
                                          LocalDateTime rangeStart, LocalDateTime rangeEnd, Integer from, Integer size);

    void deleteCommentAdmin(Long commentId);

    List<CommentShortDto> getCommentsByEvent(Long eventId, Integer from, Integer size);

    CommentShortDto getComment(Long commentId);

}
