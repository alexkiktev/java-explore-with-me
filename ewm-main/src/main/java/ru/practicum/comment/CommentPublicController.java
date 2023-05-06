package ru.practicum.comment;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.comment.dto.CommentShortDto;
import ru.practicum.comment.service.CommentService;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/comments")
@RequiredArgsConstructor
public class CommentPublicController {

    private final CommentService commentService;

    @GetMapping("/{eventId}")
    @ResponseStatus(HttpStatus.OK)
    public List<CommentShortDto> getCommentsByEvent(@PathVariable Long eventId,
                                                  @PositiveOrZero @RequestParam(name = "from",
                                                          defaultValue = "0") Integer from,
                                                  @Positive @RequestParam(name = "size",
                                                          defaultValue = "10") Integer size) {
        log.info("Получен GET-запрос для просмотра комментариев события id {}", eventId);
        return commentService.getCommentsByEvent(eventId, from, size);
    }

    @GetMapping("/{commentId}")
    @ResponseStatus(HttpStatus.OK)
    public CommentShortDto getComment(@PathVariable Long commentId) {
        log.info("Получен GET-запрос для просмотра комментария id {}", commentId);
        return commentService.getComment(commentId);
    }

}
