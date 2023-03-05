package ru.practicum.explorewithme.service;

import org.springframework.data.domain.Pageable;
import ru.practicum.explorewithme.dto.comment.CommentDto;
import ru.practicum.explorewithme.dto.comment.NewCommentDto;
import ru.practicum.explorewithme.dto.comment.UpdateCommentRequest;
import ru.practicum.explorewithme.model.ReviewStatus;

import java.util.List;

public interface CommentService {
    List<CommentDto> getUserComments(long userId, Pageable pageable);

    List<CommentDto> getEventComments(long eventId, Pageable pageable);

    List<CommentDto> getComments(ReviewStatus status, Pageable pageable);

    CommentDto createComment(long userId, NewCommentDto newComment);

    CommentDto cancelComment(long userId, long commentId);

    void deleteComment(long userId, long commentId);

    CommentDto reviewComment(long id, ReviewStatus status);

    void deleteComment(long id);

    CommentDto updateComment(long userId, long commentId, UpdateCommentRequest updateComment);
}
