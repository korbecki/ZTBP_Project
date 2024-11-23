package pl.ztbd.project.cassandra.repository;

import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.stereotype.Repository;
import pl.ztbd.project.oracle.entity.OracleFlashcardEntity;

import java.util.UUID;

@Repository
public interface CassandraFlashcardRepository extends CassandraRepository<OracleFlashcardEntity, UUID> {
}
