package nest.esprit.user.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import nest.esprit.user.Entity.User;
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Integer countByEmail(String email);
    User findByEmail(String email);
}
