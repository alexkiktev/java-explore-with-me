package ru.practicum.comment.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import ru.practicum.comment.dto.*;
import ru.practicum.comment.mapper.CommentMapper;
import ru.practicum.comment.model.ActionComment;
import ru.practicum.comment.model.Comment;
import ru.practicum.comment.model.StatusComment;
import ru.practicum.comment.repository.CommentRepository;
import ru.practicum.event.model.Event;
import ru.practicum.event.repository.EventRepository;
import ru.practicum.exception.NotFoundException;
import ru.practicum.exception.ValidationRequestException;
import ru.practicum.user.model.User;
import ru.practicum.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@Slf4j
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final CommentMapper commentMapper;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;

    @Override
    public CommentFullDto createComment(Long userId, Long eventId, CommentNewDto commentNewDto) {
        Comment comment = new Comment();
        comment.setText(commentNewDto.getText());
        comment.setAuthor(getUser(userId));
        comment.setEvent(getEvent(eventId));
        comment.setStatus(StatusComment.PENDING);
        comment.setCreated(LocalDateTime.now());
        log.info("Пользователем id {} создан комментарий к событию id {}: {}", userId, eventId, commentNewDto);
        return commentMapper.toCommentFullDto(commentRepository.save(comment));
    }

    @Override
    public CommentFullDto updateComment(Long userId, Long commentId, CommentNewDto commentNewDto) {
        getUser(userId);
        Comment comment = getCommentEntity(commentId);
        if (!Objects.equals(comment.getAuthor().getId(), userId)) {
            throw new ValidationRequestException("Only the author can change the comment.");
        }
        if (!comment.getStatus().equals(StatusComment.PENDING)) {
            throw new ValidationRequestException("Comment must have status PENDING.");
        }
        comment.setText(commentNewDto.getText());
        comment.setCreated(LocalDateTime.now());
        log.info("Пользователем id {} обновлен комментарий id {}", userId, commentId);
        return commentMapper.toCommentFullDto(commentRepository.save(comment));
    }

    @Override
    public CommentFullDto updateStatusComment(Long commentId, ActionComment actionComment) {
        Comment comment = getCommentEntity(commentId);
        if (actionComment.equals(ActionComment.PUBLISH_COMMENT)) {
            comment.setStatus(StatusComment.PUBLISHED);
        } else {
            comment.setStatus(StatusComment.REJECTED);
        }
        log.info("Администратор обновил статус комментария id {}", commentId);
        return commentMapper.toCommentFullDto(commentRepository.save(comment));
    }

    @Override
    public void deleteComment(Long userId, Long commentId) {
        getUser(userId);
        Comment comment = getCommentEntity(commentId);
        if (!Objects.equals(comment.getAuthor().getId(), userId)) {
            throw new ValidationRequestException("Only the author can delete the comment.");
        }
        commentRepository.deleteById(commentId);
        log.info("Пользователем id {} удален комментарий id {}", userId, commentId);
    }

    @Override
    public void deleteCommentAdmin(Long commentId) {
        getCommentEntity(commentId);
        commentRepository.deleteById(commentId);
        log.info("Администратором удален комментарий id {}", commentId);
    }

    @Override
    public List<CommentFullDto> getCommentsAdmin(List<Long> events, List<Long> users,
                                                 List<StatusComment> statusComments, LocalDateTime rangeStart,
                                                 LocalDateTime rangeEnd, Integer from, Integer size) {
        Pageable pageParams = PageRequest.of(from / size, size);
        List<CommentFullDto> commentFullDtos = new ArrayList<>();
        Specification<Comment> commentCriterias = CommentCriteriaQuery.getFilterCommentAdmin(events, users,
                statusComments, rangeStart, rangeEnd);
        commentRepository.findAll(commentCriterias, pageParams).getContent()
                .forEach(c -> commentFullDtos.add(commentMapper.toCommentFullDto(c)));
        log.info("Выполнен поиск комментариев администратором по заданным критериям");
        return commentFullDtos;
    }

    @Override
    public List<CommentFullDto> getCommentsByUser(Long userId, StatusComment statusComments) {
        getUser(userId);
        List<CommentFullDto> commentFullDtos = new ArrayList<>();
        if (statusComments != null) {
            commentRepository.findAllByAuthorIdAndStatus(userId, statusComments)
                    .forEach(c -> commentFullDtos.add(commentMapper.toCommentFullDto(c)));
        } else {
            commentRepository.findAllByAuthorId(userId)
                    .forEach(c -> commentFullDtos.add(commentMapper.toCommentFullDto(c)));
        }
        log.info("Получен список комментариев пользователя {} со статусом {}", userId, statusComments);
        return commentFullDtos;
    }

    @Override
    public List<CommentShortDto> getCommentsByEvent(Long eventId, Integer from, Integer size) {
        getEvent(eventId);
        List<CommentShortDto> commentShortDtos = new ArrayList<>();
        Pageable pageParams = PageRequest.of(from / size, size);
        commentRepository.findAllByEventId(eventId, pageParams)
                .forEach(c -> commentShortDtos.add(commentMapper.toCommentShortDto(c)));
        log.info("Получен список комментариев {}", commentShortDtos);
        return commentShortDtos;
    }

    @Override
    public CommentShortDto getComment(Long commentId) {
        return commentMapper.toCommentShortDto(getCommentEntity(commentId));
    }

    private User getUser(Long userId) {
        return userRepository.findById(userId).orElseThrow(() ->
                new NotFoundException(String.format("User with id=%s was not found", userId)));
    }

    private Event getEvent(Long eventId) {
        return eventRepository.findById(eventId).orElseThrow(() ->
                new NotFoundException(String.format("Event with id=%s was not found", eventId)));
    }

    private Comment getCommentEntity(Long commentId) {
        return commentRepository.findById(commentId).orElseThrow(() ->
                new NotFoundException(String.format("Comment with id=%s was not found", commentId)));
    }

}
