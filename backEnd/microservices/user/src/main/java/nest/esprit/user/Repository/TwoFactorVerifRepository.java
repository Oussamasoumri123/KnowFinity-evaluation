package nest.esprit.user.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import nest.esprit.user.Entity.TwoFactorVerif;
@Repository
public interface TwoFactorVerifRepository extends JpaRepository<TwoFactorVerif, Long> {
    @Query("SELECT t FROM TwoFactorVerif t WHERE t.user.email = :email")
    TwoFactorVerif getTwoFactorVerifByUserEmail(String email);
}
