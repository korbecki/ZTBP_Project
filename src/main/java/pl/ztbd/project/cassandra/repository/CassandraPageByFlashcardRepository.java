package pl.ztbd.project.cassandra.repository;

import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.stereotype.Repository;
import pl.ztbd.project.cassandra.entity.CassandraPageByFlashcardEntity;
import pl.ztbd.project.cassandra.entity.key.PageByFlashcardEntityKey;

@Repository
public interface CassandraPageByFlashcardRepository extends CassandraRepository<CassandraPageByFlashcardEntity, PageByFlashcardEntityKey> {
}
