package pl.ztbd.project.oracle.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.ztbd.project.oracle.entity.OracleRefreshTokenEntity;

import java.util.Optional;

@Repository
public interface OracleRefreshTokenRepository extends JpaRepository<OracleRefreshTokenEntity, Long> {
    void deleteAllByUserId(Long userId);


    Optional<OracleRefreshTokenEntity> findByUserId(Long userId);
}
