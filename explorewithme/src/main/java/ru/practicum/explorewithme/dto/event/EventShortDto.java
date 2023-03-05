package ru.practicum.explorewithme.dto.event;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import ru.practicum.explorewithme.dto.category.CategoryDto;
import ru.practicum.explorewithme.dto.user.UserShortDto;
import ru.practicum.explorewithme.utils.DateUtils;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventShortDto {
    private String annotation;

    private CategoryDto category;

    private long confirmedRequests;

    @JsonFormat(pattern = DateUtils.DATE_FORMAT)
    @DateTimeFormat(pattern = DateUtils.DATE_FORMAT)
    private LocalDateTime eventDate;

    private long id;

    private UserShortDto initiator;

    private boolean paid;

    private String title;

    private long views;
}
