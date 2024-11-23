package pl.ztbd.project.oracle.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.ztbd.project.oracle.entity.OracleUserEntity;

import java.util.Optional;

@Repository
public interface OracleUserRepository extends JpaRepository<OracleUserEntity, Long> {
    boolean existsByEmail(String email);

    Optional<OracleUserEntity> findByEmail(String email);
}
