package ru.practicum.explorewithme.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.explorewithme.model.Comment;
import ru.practicum.explorewithme.model.ReviewStatus;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findAllByAuthorId(long authorId, Pageable pageable);

    List<Comment> findAllByState(ReviewStatus state, Pageable pageable);

    List<Comment> findAllByEventIdAndState(Long eventId, ReviewStatus state, Pageable pageable);
}
