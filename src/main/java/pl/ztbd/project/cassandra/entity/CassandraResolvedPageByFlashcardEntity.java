package pl.ztbd.project.cassandra.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldNameConstants;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;
import pl.ztbd.project.cassandra.entity.key.ResolvedPageByFlashcardEntityKey;

import java.time.OffsetDateTime;

@Data
@AllArgsConstructor
@FieldNameConstants
@NoArgsConstructor
@Builder
@Table(value = "resolved_page_by_flashcard")
public class CassandraResolvedPageByFlashcardEntity {

    @PrimaryKey
    private ResolvedPageByFlashcardEntityKey resolvedPageByFlashcardEntity;

    @Column(value = "answer")
    private String answer;

    @Column(value = "is_correct")
    private Boolean isCorrect;

    @Column(value = "created_at")
    private OffsetDateTime createdAt;
}
