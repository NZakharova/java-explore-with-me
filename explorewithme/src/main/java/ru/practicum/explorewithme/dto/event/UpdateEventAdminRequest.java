package ru.practicum.explorewithme.dto.event;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class UpdateEventAdminRequest extends UpdateEventRequestBase {
    private AdminStateAction stateAction;
}

