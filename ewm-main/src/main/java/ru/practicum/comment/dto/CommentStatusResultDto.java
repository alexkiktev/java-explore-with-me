package ru.practicum.comment.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentStatusResultDto {
    List<CommentFullDto> pendingComments;
    List<CommentFullDto> publishedComments;
    List<CommentFullDto> rejectedComments;
}
