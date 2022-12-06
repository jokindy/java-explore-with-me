package ru.practicum.explorewithme.compilation;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import ru.practicum.explorewithme.event.Event;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.Set;

@Getter
@Setter
@ToString
@Entity
@Table(name = "compilations")
@NoArgsConstructor
public class Compilation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @Size(min = 5, max = 120)
    private String title;

    @Column(nullable = false)
    private boolean pinned;

    @ManyToMany
    @JoinTable(
            name = "event_compilations",
            joinColumns = @JoinColumn(name = "compilation_id"),
            inverseJoinColumns = @JoinColumn(name = "event_id"))
    @ToString.Exclude
    private Set<Event> events;
}
