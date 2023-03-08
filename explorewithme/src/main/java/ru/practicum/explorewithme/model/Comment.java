package ru.practicum.explorewithme.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "comments")
@NoArgsConstructor
@AllArgsConstructor
public class Comment {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "text")
    private String text;

    @NotNull
    @Column(name = "author_id")
    private Long authorId;

    @NotNull
    @Column(name = "event_id")
    private Long eventId;

    @NotNull
    @Column(name = "created_on")
    private LocalDateTime createdOn;

    @Column(name = "updated_on")
    private LocalDateTime updatedOn;

    @NotNull
    @Column(name = "state")
    @Enumerated(EnumType.ORDINAL)
    private ReviewStatus state;
}
