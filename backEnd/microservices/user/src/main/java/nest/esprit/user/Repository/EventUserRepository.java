package nest.esprit.user.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import nest.esprit.user.Entity.EventUser;

@Repository
public interface EventUserRepository extends JpaRepository<EventUser, Long> {
}
