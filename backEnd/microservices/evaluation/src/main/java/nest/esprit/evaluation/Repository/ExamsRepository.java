package nest.esprit.evaluation.Repository;

import nest.esprit.evaluation.Entity.Exams;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExamsRepository extends JpaRepository<Exams, Integer> {
}
