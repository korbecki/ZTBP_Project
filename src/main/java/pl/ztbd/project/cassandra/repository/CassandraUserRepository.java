package pl.ztbd.project.cassandra.repository;

import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.stereotype.Repository;
import pl.ztbd.project.cassandra.entity.CassandraUserEntity;

import java.util.Optional;

@Repository
public interface CassandraUserRepository extends CassandraRepository<CassandraUserEntity, String> {
    Optional<CassandraUserEntity> findByEmail(String email);

    boolean existsByEmail(String email);
}
