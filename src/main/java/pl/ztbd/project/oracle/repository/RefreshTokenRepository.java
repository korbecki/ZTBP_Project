package pl.ztbd.project.oracle.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.ztbd.project.oracle.entity.RefreshTokenEntity;

import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshTokenEntity, Long> {
    boolean deleteAllByUserId(Long userId);


    Optional<RefreshTokenEntity> findByUserId(Long userId);
}
