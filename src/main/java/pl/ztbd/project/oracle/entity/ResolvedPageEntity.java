package pl.ztbd.project.oracle.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.OffsetDateTime;

@Table(name = "RESOLVED_PAGE", schema = "FLASHCARDS_USER")
@Entity
@Setter
@Getter
public class ResolvedPageEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "RESOLVED_PAGE-GENERATOR")
    @SequenceGenerator(name = "RESOLVED_PAGE-GENERATOR", sequenceName = "resolved_page_seq", allocationSize = 1)
    @Column(name = "RESOLVED_PAGE_ID")
    private Long id;

    @Column(name = "USER_ID")
    private Long userId;

    @Column(name = "FLASHCARD_PAGE_ID")
    private Long flashcardPageId;

    @Column(name = "ANSWER")
    private String answer;

    @Column(name = "IS_CORRECT")
    private Boolean isCorrect;

    @Column(name = "CREATED_AT")
    private OffsetDateTime createdAt;

    public ResolvedPageEntity(Long userId, Long flashcardPageId, String answer, Boolean isCorrect) {
        this.userId = userId;
        this.flashcardPageId = flashcardPageId;
        this.answer = answer;
        this.isCorrect = isCorrect;
        createdAt = OffsetDateTime.now();
    }
}