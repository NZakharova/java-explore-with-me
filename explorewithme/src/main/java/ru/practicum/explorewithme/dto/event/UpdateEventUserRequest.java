package ru.practicum.explorewithme.dto.event;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class UpdateEventUserRequest extends UpdateEventRequestBase {
    private UserStateAction stateAction;
}
