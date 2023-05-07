package ru.practicum.comment;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.comment.dto.CommentFullDto;
import ru.practicum.comment.dto.CommentNewDto;
import ru.practicum.comment.model.StatusComment;
import ru.practicum.comment.service.CommentService;

import javax.validation.Valid;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/users/{userId}/comments")
@RequiredArgsConstructor
public class CommentPrivateController {

    private final CommentService commentService;

    @PostMapping("/{eventId}")
    @ResponseStatus(HttpStatus.CREATED)
    public CommentFullDto createComment(@PathVariable Long userId, @PathVariable Long eventId,
                                        @Valid @RequestBody CommentNewDto commentNewDto) {
        log.info("Получен POST-запрос на создание комментария к событию id {} от пользователя id {}", eventId, userId);
        return commentService.createComment(userId, eventId, commentNewDto);
    }

    @PatchMapping("/{commentId}")
    @ResponseStatus(HttpStatus.OK)
    public CommentFullDto updateComment(@PathVariable Long userId, @PathVariable Long commentId,
                                        @Valid @RequestBody CommentNewDto commentNewDto) {
        log.info("Получен PATCH-запрос для изменения комментария id {}, созданного пользователем id {}", commentId,
                userId);
        return commentService.updateComment(userId, commentId, commentNewDto);
    }

    @DeleteMapping("/{commentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteComment(@PathVariable Long userId, @PathVariable Long commentId) {
        log.info("Получен DELETE-запрос для удаления комментария id {} от пользователя id {}", commentId, userId);
        commentService.deleteComment(userId, commentId);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<CommentFullDto> getCommentsByUser(@PathVariable Long userId,
                                                    @RequestParam(name = "status",
                                                            required = false) StatusComment statusComments) {
        log.info("Получен GET-запрос для вывода всех собственных комментариев пользователя id {} со статусом {}",
                userId, statusComments);
        return commentService.getCommentsByUser(userId, statusComments);
    }

}