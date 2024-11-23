package pl.ztbd.project.oracle.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.ztbd.project.oracle.entity.OracleResolvedPageEntity;

@Repository
public interface OracleResolvedPageRepository extends JpaRepository<OracleResolvedPageEntity, Long> {

    void deleteAllByUserId(Long userId);
}
