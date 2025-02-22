package nest.esprit.user.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import nest.esprit.user.Entity.AccountVerifications;

@Repository
public interface AccountVerificationsRepository extends JpaRepository<AccountVerifications, Long> {
    AccountVerifications findByUrl(String email);
}
