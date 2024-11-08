package pl.ztbd.project.cassandra.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldNameConstants;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;
import pl.ztbd.project.cassandra.entity.key.PageByFlashcardEntityKey;

import java.time.OffsetDateTime;

@Data
@AllArgsConstructor
@FieldNameConstants
@NoArgsConstructor
@Builder
@Table(value = "page_by_flashcard")
public class PageByFlashcardEntity {

    @PrimaryKey
    private PageByFlashcardEntityKey pageByFlashcardEntityKey;

    @Column(value = "question")
    private String question;

    @Column(value = "answer")
    private String answer;

    @Column(value = "created_at")
    private OffsetDateTime createdAt;
}
