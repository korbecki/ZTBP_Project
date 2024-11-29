package pl.ztbd.project.cassandra.repository;

import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.stereotype.Repository;
import pl.ztbd.project.cassandra.entity.CassandraResolvedPageByFlashcardEntity;

import java.util.List;

@Repository
public interface CassandraResolvedPageByFlashcardRepository extends CassandraRepository<CassandraResolvedPageByFlashcardEntity, CassandraResolvedPageByFlashcardEntity> {
    List<CassandraResolvedPageByFlashcardEntity> findByUserId(String userId);

    void deleteAllByUserId(String userId);
}
