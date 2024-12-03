package pl.ztbd.project.cassandra.repository;

import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.stereotype.Repository;
import pl.ztbd.project.cassandra.entity.CassandraPageByFlashcardEntity;
import pl.ztbd.project.cassandra.entity.key.PageByFlashcardEntityKey;

import java.util.List;
import java.util.UUID;

@Repository
public interface CassandraPageByFlashcardRepository extends CassandraRepository<CassandraPageByFlashcardEntity, PageByFlashcardEntityKey> {
    List<CassandraPageByFlashcardEntity> findAllByPageByFlashcardEntityKey_FlashcardId(UUID flashcardId);
}
