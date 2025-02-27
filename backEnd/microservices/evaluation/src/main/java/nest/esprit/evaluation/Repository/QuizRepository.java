package nest.esprit.evaluation.Repository;

import nest.esprit.evaluation.Entity.Quiz;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuizRepository extends JpaRepository<Quiz, Integer> {
}
