package pl.ztbd.project.cassandra.repository;

import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.stereotype.Repository;
import pl.ztbd.project.oracle.entity.FlashcardEntity;

import java.util.UUID;

@Repository
public interface FlashcardRepository extends CassandraRepository<FlashcardEntity, UUID> {
}
