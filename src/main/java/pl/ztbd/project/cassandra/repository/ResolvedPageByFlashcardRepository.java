package pl.ztbd.project.cassandra.repository;

import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.stereotype.Repository;
import pl.ztbd.project.cassandra.entity.ResolvedPageByFlashcardEntity;
import pl.ztbd.project.cassandra.entity.UserEntity;

@Repository
public interface ResolvedPageByFlashcardRepository extends CassandraRepository<ResolvedPageByFlashcardEntity, ResolvedPageByFlashcardEntity> {
}
