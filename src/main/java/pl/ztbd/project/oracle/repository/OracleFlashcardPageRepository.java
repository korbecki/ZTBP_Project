package pl.ztbd.project.oracle.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.ztbd.project.oracle.entity.OracleFlashcardPageEntity;

import java.util.List;

@Repository
public interface OracleFlashcardPageRepository extends JpaRepository<OracleFlashcardPageEntity, Long> {
    List<OracleFlashcardPageEntity> findAllByFlashcardId(Long flashcardId);
}
