package ru.practicum.explorewithme.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import ru.practicum.explorewithme.utils.DateUtils;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiError {
    private List<String> errors;

    private String message;

    private String reason;

    private String status;

    @JsonFormat(pattern = DateUtils.DATE_FORMAT)
    @DateTimeFormat(pattern = DateUtils.DATE_FORMAT)
    private LocalDateTime timestamp;
}
