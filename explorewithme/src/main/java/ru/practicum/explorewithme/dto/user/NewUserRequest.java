package ru.practicum.explorewithme.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NewUserRequest {
    @Email
    @NotNull
    private String email;

    @NotNull
    @NotBlank
    private String name;
}
