package pl.ztbd.project.oracle.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.ztbd.project.oracle.entity.ResolvedPageEntity;

@Repository
public interface ResolvedPageRepository extends JpaRepository<ResolvedPageEntity, Long> {

    void deleteAllByUserId(Long userId);
}
