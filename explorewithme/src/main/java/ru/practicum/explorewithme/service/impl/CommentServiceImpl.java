package ru.practicum.explorewithme.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.explorewithme.dto.comment.CommentDto;
import ru.practicum.explorewithme.dto.comment.NewCommentDto;
import ru.practicum.explorewithme.dto.comment.UpdateCommentRequest;
import ru.practicum.explorewithme.exceptions.ConflictException;
import ru.practicum.explorewithme.exceptions.ObjectNotFoundException;
import ru.practicum.explorewithme.model.Comment;
import ru.practicum.explorewithme.model.ReviewStatus;
import ru.practicum.explorewithme.repository.CommentRepository;
import ru.practicum.explorewithme.repository.EventRepository;
import ru.practicum.explorewithme.repository.ParticipationRequestRepository;
import ru.practicum.explorewithme.service.CommentService;
import ru.practicum.explorewithme.service.mappers.CommentMapper;
import ru.practicum.explorewithme.utils.DateUtils;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;
    private final EventRepository eventRepository;
    private final ParticipationRequestRepository participationRequestRepository;

    @Override
    @Transactional(readOnly = true)
    public List<CommentDto> getUserComments(long userId, Pageable pageable) {
        return toDto(commentRepository.findAllByAuthorId(userId, pageable));
    }

    @Override
    @Transactional(readOnly = true)
    public List<CommentDto> getEventComments(long eventId, Pageable pageable) {
        return toDto(commentRepository.findAllByEventIdAndState(eventId, ReviewStatus.CONFIRMED, pageable));
    }

    @Override
    @Transactional(readOnly = true)
    public List<CommentDto> getComments(ReviewStatus status, Pageable pageable) {
        if (status != null) {
            return toDto(commentRepository.findAllByState(status, pageable));
        } else {
            return toDto(commentRepository.findAll(pageable).toList());
        }
    }

    @Override
    @Transactional
    public CommentDto createComment(long userId, NewCommentDto newComment) {
        var event = eventRepository.findById(newComment.getEventId()).orElseThrow(() -> new ObjectNotFoundException("Event", newComment.getEventId()));
        var now = DateUtils.now();
        if (event.getEventDate().isAfter(now)) {
            throw new ConflictException("Cannot leave comment for event that didn't occurred");
        }

        var participation = participationRequestRepository.findByEventIdAndRequesterId(newComment.getEventId(), userId).orElseThrow(() -> new ObjectNotFoundException("ParticipationRequest", -1L));
        if (participation.getState() != ReviewStatus.CONFIRMED) {
            throw new ConflictException("Cannot leave comment for event when request wasn't confirmed");
        }

        var comment = new Comment(null, newComment.getText(), userId, event.getId(), now, now, ReviewStatus.PENDING);

        commentRepository.save(comment);

        return CommentMapper.toDto(comment);
    }

    @Override
    @Transactional
    public CommentDto cancelComment(long userId, long commentId) {
        var comment = getCommentModel(userId, commentId);

        if (comment.getState() != ReviewStatus.PENDING) {
            throw new ConflictException("Can only cancel comments in 'PENDING' state");
        }

        comment.setState(ReviewStatus.CANCELED);

        commentRepository.save(comment);

        return CommentMapper.toDto(comment);
    }

    @Override
    @Transactional
    public void deleteComment(long userId, long commentId) {
        var comment = getCommentModel(userId, commentId);
        commentRepository.delete(comment);
    }

    @Override
    @Transactional
    public CommentDto reviewComment(long id, ReviewStatus status) {
        var comment = getCommentModel(id);
        if (comment.getState() != ReviewStatus.PENDING) {
            throw new ConflictException("Comment review is already done");
        }

        switch (status) {
            case CONFIRMED:
            case REJECTED:
            case CANCELED:
                comment.setState(status);
                commentRepository.save(comment);
                break;
            default:
                throw new ConflictException("Invalid review status: " + status);
        }

        return CommentMapper.toDto(comment);
    }

    @Override
    @Transactional
    public void deleteComment(long id) {
        commentRepository.deleteById(id);
    }

    @Override
    @Transactional
    public CommentDto updateComment(long userId, long commentId, UpdateCommentRequest updateComment) {
        var comment = getCommentModel(userId, commentId);
        if (comment.getState() != ReviewStatus.PENDING) {
            throw new ConflictException("Cannot change comment when review is already done");
        }

        comment.setText(updateComment.getText());

        commentRepository.save(comment);

        return CommentMapper.toDto(comment);
    }

    private List<CommentDto> toDto(List<Comment> comments) {
        return comments.stream().map(CommentMapper::toDto).collect(Collectors.toList());
    }

    private Comment getCommentModel(long userId, long commentId) {
        var comment = getCommentModel(commentId);
        if (comment.getAuthorId() != userId) {
            throw new ObjectNotFoundException("Comment", commentId);
        }

        return comment;
    }

    private Comment getCommentModel(long commentId) {
        return commentRepository.findById(commentId).orElseThrow(() -> new ObjectNotFoundException("Comment", commentId));
    }
}
