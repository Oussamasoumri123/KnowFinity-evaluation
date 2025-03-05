package nest.esprit.user.Entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "users", uniqueConstraints = {
        @UniqueConstraint(columnNames = "email"),
        @UniqueConstraint(columnNames = "phone")
})
@Data
@NoArgsConstructor
@AllArgsConstructor

public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false)
    private Long id;

    @Column(name = "first_name", length = 50, nullable = false)
    private String firstName;

    @Column(name = "last_name", length = 50, nullable = false)
    private String lastName;

    @Column(name = "email", length = 100, nullable = false, unique = true)
    private String email;

    @Column(name = "password", length = 255)
    private String password;

    @Column(name = "address", length = 255)
    private String address;

    @Column(name = "phone", length = 30)
    private String phone;

    @Column(name = "title", length = 50)
    private String title;

    @Column(name = "bio", length = 255)
    private String bio;

    @Column(name = "enabled", nullable = false)
    private Boolean enabled = false;

    @Column(name = "non_locked", nullable = false)
    private Boolean nonLocked = true;

    @Column(name = "using_mfa", nullable = false)
    private Boolean usingMfa = false;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();
    @Column(name = "image_url", nullable = false)
    private String imageUrl = "https://cdn-icons-png.flaticon.com/512/149/149071.png";
    @ManyToMany(fetch = FetchType.EAGER,mappedBy = "users")
    @JsonIgnore
   private List<Role> role;
    @OneToOne(mappedBy = "user", orphanRemoval = true, cascade = CascadeType.ALL)
   ResetPassword resetPassword;
    @OneToOne(mappedBy = "user",cascade = CascadeType.ALL)
    @JsonIgnore
   AccountVerifications accountVerifications;
    @OneToOne(mappedBy = "user", orphanRemoval = true)
    TwoFactorVerif twoFactorVerif;
    @OneToMany(mappedBy = "user")
    List<EventUser> eventUsers;
}

