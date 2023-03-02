package ru.practicum.explorewithme.statistics.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import ru.practicum.explorewithme.utils.DateUtils;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EndpointHit {
    private Long id;
    private String app;
    private String uri;
    private String ip;

    @JsonFormat(pattern = DateUtils.DATE_FORMAT)
    @DateTimeFormat(pattern = DateUtils.DATE_FORMAT)
    private LocalDateTime timestamp;
}
