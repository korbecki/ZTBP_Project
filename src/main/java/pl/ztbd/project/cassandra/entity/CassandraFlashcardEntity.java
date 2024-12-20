package pl.ztbd.project.cassandra.entity;

import com.datastax.oss.driver.api.core.uuid.Uuids;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldNameConstants;
import org.springframework.data.annotation.Id;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.CassandraType;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.core.mapping.Table;

import java.util.UUID;

@Data
@AllArgsConstructor
@FieldNameConstants
@NoArgsConstructor
@Builder
@Table(value = "flashcard")
public class CassandraFlashcardEntity {

    @Id
    @CassandraType(type = CassandraType.Name.TIMEUUID)
    @PrimaryKeyColumn(ordinal = 0, type = PrimaryKeyType.PARTITIONED, value = "flashcard_id")
    @Builder.Default
    private UUID id = Uuids.timeBased();

    @Column(value = "name")
    private String name;

    @Column(value = "description")
    private String description;
}
