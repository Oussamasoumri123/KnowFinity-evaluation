package nest.esprit.user.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import nest.esprit.user.Entity.ResetPassword;
import nest.esprit.user.Entity.User;

@Repository
public interface ResetPasswordRepository extends JpaRepository<ResetPassword,Long> {
    ResetPassword findByUser(User user);
    ResetPassword findByUrl(String url);
    @Query("SELECT t.user FROM ResetPassword t WHERE t.url = :url")
    User findUserByUrl(String url);
}
