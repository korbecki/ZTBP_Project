package pl.ztbd.project.cassandra.repository;

import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.stereotype.Repository;
import pl.ztbd.project.cassandra.entity.PageByFlashcardEntity;
import pl.ztbd.project.cassandra.entity.key.PageByFlashcardEntityKey;

@Repository
public interface PageByFlashcardRepository extends CassandraRepository<PageByFlashcardEntity, PageByFlashcardEntityKey> {
}
