package nest.esprit.user.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "Reset_Password", uniqueConstraints = {
        @UniqueConstraint(columnNames = "Url")})
public class ResetPassword {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "url", nullable = false)
    private String url;
    @Column(name = "date", nullable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date expirationDate = Date.from(LocalDate.now().plusDays(3)
                    .atStartOfDay(ZoneId.systemDefault())
                    .toInstant());
    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
