package pl.ztbd.project.oracle.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.ztbd.project.oracle.entity.OracleFlashcardEntity;

@Repository
public interface OracleFlashcardRepository extends JpaRepository<OracleFlashcardEntity, Long> {
}
