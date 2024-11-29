package pl.ztbd.project.cassandra.repository;

import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.stereotype.Repository;
import pl.ztbd.project.cassandra.entity.CassandraRefreshTokenEntity;
import pl.ztbd.project.cassandra.entity.key.RefreshTokenKey;

import java.util.Optional;

@Repository
public interface CassandraRefreshTokenRepository extends CassandraRepository<CassandraRefreshTokenEntity, RefreshTokenKey> {
    Optional<CassandraRefreshTokenEntity> findByUserEmail(String userEmail);

    boolean deleteAllByUserEmail(String userEmail);
}
