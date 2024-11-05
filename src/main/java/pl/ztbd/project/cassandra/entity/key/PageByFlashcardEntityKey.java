package pl.ztbd.project.cassandra.entity.key;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.FieldNameConstants;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.CassandraType;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyClass;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;

import java.util.UUID;

@PrimaryKeyClass
@FieldNameConstants
@Data
@AllArgsConstructor
public class PageByFlashcardEntityKey {

    @PrimaryKeyColumn(ordinal = 0, type = PrimaryKeyType.PARTITIONED, name = "flashcard_id")
    @CassandraType(type = CassandraType.Name.TIMEUUID)
    private UUID flashcardId;

    @PrimaryKeyColumn(ordinal = 1, type = PrimaryKeyType.CLUSTERED, name = "flashcard_page_id")
    @CassandraType(type = CassandraType.Name.TIMEUUID)
    private UUID flashcardPageId;
}
