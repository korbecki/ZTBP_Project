package pl.ztbd.project.oracle.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;


@Table(name = "FLASHCARD", schema = "FLASHCARDS_USER")
@Entity
@Getter
@Setter
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
}