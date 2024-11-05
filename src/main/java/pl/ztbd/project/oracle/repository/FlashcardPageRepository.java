package pl.ztbd.project.oracle.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.ztbd.project.oracle.entity.FlashcardPageEntity;

import java.util.List;

@Repository
public interface FlashcardPageRepository extends JpaRepository<FlashcardPageEntity, Long> {
    List<FlashcardPageEntity> findAllByFlashcardId(Long flashcardId);
}
