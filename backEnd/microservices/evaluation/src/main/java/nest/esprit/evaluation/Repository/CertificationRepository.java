package nest.esprit.evaluation.Repository;

import nest.esprit.evaluation.Entity.Certification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CertificationRepository extends JpaRepository<Certification, Integer> {
}
