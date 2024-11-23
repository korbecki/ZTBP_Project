package pl.ztbd.project.oracle.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.OffsetDateTime;

@Table(name = "FLASHCARD_PAGE", schema = "FLASHCARDS_USER")
@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class OracleFlashcardPageEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "FLASHCARD_PAGE-GENERATOR")
    @SequenceGenerator(name = "FLASHCARD_PAGE-GENERATOR", sequenceName = "flashcard_page_seq", allocationSize = 1)
    @Column(name = "FLASHCARD_PAGE_ID")
    private Long id;

    @Column(name = "FLASHCARD_ID")
    private Long flashcardId;

    @Column(name = "QUESTION")
    private String question;

    @Column(name = "ANSWER")
    private String answer;

    @Column(name = "CREATED_AT")
    private OffsetDateTime createdAt;

    public OracleFlashcardPageEntity(Long flashcardId, String question, String answer) {
        this.flashcardId = flashcardId;
        this.question = question;
        this.answer = answer;
        this.createdAt = OffsetDateTime.now();
    }
}