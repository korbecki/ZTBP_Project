package pl.ztbd.project.cassandra.repository;

import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.stereotype.Repository;
import pl.ztbd.project.cassandra.entity.UserEntity;

@Repository
public interface UserRepository extends CassandraRepository<UserEntity, String> {
}
