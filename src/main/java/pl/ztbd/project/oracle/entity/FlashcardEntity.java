package pl.ztbd.project.oracle.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Table(name = "FLASHCARD", schema = "FLASHCARDS_USER")
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FlashcardEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "FLASHCARD-GENERATOR")
    @SequenceGenerator(name = "FLASHCARD-GENERATOR", sequenceName = "users_seq", allocationSize = 1)
    @Column(name = "FLASHCARD_ID")
    private Long id;

    @Column(name = "NAME")
    private String name;

    @Column(name = "DESCRIPTION")
    private String description;

    public FlashcardEntity(String name, String description) {
        this.name = name;
        this.description = description;
    }
}