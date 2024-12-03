package pl.ztbd.project.cassandra.repository;

import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.stereotype.Repository;
import pl.ztbd.project.cassandra.entity.CassandraResolvedPageByFlashcardEntity;

import java.util.List;
import java.util.UUID;

@Repository
public interface CassandraResolvedPageByFlashcardRepository extends CassandraRepository<CassandraResolvedPageByFlashcardEntity, CassandraResolvedPageByFlashcardEntity> {
    List<CassandraResolvedPageByFlashcardEntity> findAllByResolvedPageByFlashcardEntityKey_UserEmail(String userEmail);
    void deleteAllByResolvedPageByFlashcardEntityKey_UserEmailAndResolvedPageByFlashcardEntityKey_FlashcardPageId(String email, UUID pageId);
    void deleteAllByResolvedPageByFlashcardEntityKey_UserEmail(String userEmail);
}
