package ru.practicum.explorewithme.dto.comment;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateCommentRequest {
    @Size(min = 5, max = 2000)
    private String text;
}
