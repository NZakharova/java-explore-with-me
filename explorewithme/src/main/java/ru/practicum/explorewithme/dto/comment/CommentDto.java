package ru.practicum.explorewithme.dto.comment;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import ru.practicum.explorewithme.model.ReviewStatus;
import ru.practicum.explorewithme.utils.DateUtils;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentDto {
    private long id;

    private long authorId;

    private long eventId;

    @JsonFormat(pattern = DateUtils.DATE_FORMAT)
    @DateTimeFormat(pattern = DateUtils.DATE_FORMAT)
    private LocalDateTime createdOn;

    @JsonFormat(pattern = DateUtils.DATE_FORMAT)
    @DateTimeFormat(pattern = DateUtils.DATE_FORMAT)
    private LocalDateTime updatedOn;

    private String text;

    private ReviewStatus state;
}
