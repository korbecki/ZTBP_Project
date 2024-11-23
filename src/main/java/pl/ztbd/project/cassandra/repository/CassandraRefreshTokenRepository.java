package pl.ztbd.project.cassandra.repository;

import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.stereotype.Repository;
import pl.ztbd.project.cassandra.entity.CassandraRefreshTokenEntity;
import pl.ztbd.project.cassandra.entity.key.RefreshTokenKey;

@Repository
public interface CassandraRefreshTokenRepository extends CassandraRepository<CassandraRefreshTokenEntity, RefreshTokenKey> {
}
