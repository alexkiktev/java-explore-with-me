package ru.practicum.comment.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.practicum.comment.model.ActionComment;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
public class CommentStatusRequestDto {
    @NotBlank
    private ActionComment actionComment;
}
