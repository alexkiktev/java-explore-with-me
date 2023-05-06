package ru.practicum.comment;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.comment.dto.CommentFullDto;
import ru.practicum.comment.dto.CommentStatusRequestDto;
import ru.practicum.comment.model.StatusComment;
import ru.practicum.comment.service.CommentService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/admin/comments")
@RequiredArgsConstructor
public class CommentAdminController {

    private final CommentService commentService;

    @PatchMapping("/{commentId}")
    @ResponseStatus(HttpStatus.OK)
    public CommentFullDto updateStatusComment(@PathVariable Long commentId,
                                              @Valid @RequestBody CommentStatusRequestDto commentStatusRequestDto) {
        log.info("Получен PATCH-запрос на изменение статуса комментария id {}", commentId);
        return commentService.updateStatusComment(commentId, commentStatusRequestDto);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<CommentFullDto> getCommentsAdmin(@RequestParam(name = "events", required = false) List<Long> events,
                                                 @RequestParam(name = "users", required = false) List<Long> users,
                                                 @RequestParam(name = "statuses",
                                                     required = false) List<StatusComment> statusComments,
                                                 @RequestParam(name = "rangeStart",
                                                     required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
                                                 LocalDateTime rangeStart,
                                                 @RequestParam(name = "rangeEnd",
                                                     required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
                                                 LocalDateTime rangeEnd,
                                                 @PositiveOrZero @RequestParam(defaultValue = "0") Integer from,
                                                 @Positive @RequestParam(defaultValue = "10") Integer size) {
        log.info("Получен GET-запрос на вывод комментариев с параметрами");
        return commentService.getCommentsAdmin(events, users, statusComments, rangeStart, rangeEnd, from,
                size);
    }

    @DeleteMapping("/{commentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCommentAdmin(@PathVariable Long commentId) {
        log.info("Получен DELETE-запрос администратора для удаления комментария id {}", commentId);
        commentService.deleteCommentAdmin(commentId);
    }

}
