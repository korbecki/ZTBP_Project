package pl.ztbd.project.cassandra.repository;

import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.stereotype.Repository;
import pl.ztbd.project.cassandra.entity.RefreshTokenEntity;
import pl.ztbd.project.cassandra.entity.key.RefreshTokenKey;

@Repository
public interface RefreshTokenRepository extends CassandraRepository<RefreshTokenEntity, RefreshTokenKey> {
}
