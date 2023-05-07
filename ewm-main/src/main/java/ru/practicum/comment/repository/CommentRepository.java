package ru.practicum.comment.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import ru.practicum.comment.model.Comment;
import ru.practicum.comment.model.StatusComment;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long>, JpaSpecificationExecutor<Comment> {
    List<Comment> findAllByEventId(Long eventId, Pageable pageParams);

    List<Comment> findAllByAuthorIdAndStatus(Long userId, StatusComment statusComments);

    List<Comment> findAllByAuthorId(Long userId);

    Page<Comment> findAll(Specification<Comment> commentCriterias, Pageable pageParams);

    List<Comment> findAllByEventIdAndStatus(Long id, StatusComment statusComments);

}
