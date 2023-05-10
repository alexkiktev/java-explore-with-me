package ru.practicum.comment.service;

import org.springframework.data.jpa.domain.Specification;
import ru.practicum.comment.model.Comment;
import ru.practicum.comment.model.StatusComment;

import javax.persistence.criteria.Predicate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class CommentCriteriaQuery {

    public static Specification<Comment> getFilterCommentAdmin(List<Long> events, List<Long> users,
                                                               List<StatusComment> statusComments,
                                                               LocalDateTime rangeStart, LocalDateTime rangeEnd) {
        return (root, criteriaQuery, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (events != null) {
                if (events.get(0) != 0) {
                    predicates.add(root.get("event").get("id").in(events));
                }
            }
            if (users != null) {
                if (users.get(0) != 0) {
                    predicates.add(root.get("author").in(users));
                }
            }
            if (statusComments != null) {
                predicates.add(root.get("status").in(statusComments));
            }
            if (rangeStart != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("created"), rangeStart));
            }
            if (rangeEnd != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("created"), rangeEnd));
            }
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }

}
