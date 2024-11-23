package pl.ztbd.project.cassandra.repository;

import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.stereotype.Repository;
import pl.ztbd.project.cassandra.entity.CassandraResolvedPageByFlashcardEntity;

@Repository
public interface CassandraResolvedPageByFlashcardRepository extends CassandraRepository<CassandraResolvedPageByFlashcardEntity, CassandraResolvedPageByFlashcardEntity> {
}
