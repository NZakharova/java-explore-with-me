package ru.practicum.explorewithme.controllers.adminapi;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explorewithme.dto.comment.CommentDto;
import ru.practicum.explorewithme.model.ReviewStatus;
import ru.practicum.explorewithme.service.CommentService;
import ru.practicum.explorewithme.statistics.StatisticsClient;
import ru.practicum.explorewithme.utils.PaginationUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/admin/comments")
@RequiredArgsConstructor
public class AdminCommentController {
    private final CommentService commentService;
    private final StatisticsClient statisticsClient;

    // администратор может получать комментарии, находящиеся в определённом статусе.
    // Обычно это будет PENDING, чтобы проверить их. Если статус не указан - возвращает все комментарии
    @GetMapping
    public List<CommentDto> getComments(@RequestParam(required = false) ReviewStatus status,
                                        @RequestParam(defaultValue = "0") int from,
                                        @RequestParam(defaultValue = "10") int size,
                                        HttpServletRequest request) {
        log.info("Admin: get comments in status {}", status);
        statisticsClient.registerHit(request);
        return commentService.getComments(status, PaginationUtils.create(from, size));
    }

    // администратор может проверять комментарии, не прошедшие проверку
    @PatchMapping("/{id}")
    public CommentDto reviewComment(@PathVariable long id, @RequestParam ReviewStatus status, HttpServletRequest request) {
        log.info("Admin: review comment {}: status {}", id, status);
        statisticsClient.registerHit(request);
        return commentService.reviewComment(id, status);
    }

    // администратор может удалять любые комменатрии
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteComment(@PathVariable long id, HttpServletRequest request) {
        log.info("Admin: delete comment {}", id);
        statisticsClient.registerHit(request);
        commentService.deleteComment(id);
    }
}
