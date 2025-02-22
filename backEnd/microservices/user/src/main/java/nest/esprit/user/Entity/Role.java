package nest.esprit.user.Entity;

import jakarta.persistence.*;
import lombok.Data;
import nest.esprit.user.Entity.enumeration.RoleType;

import java.util.List;

@Entity
@Table(name = "Role")
@Data
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, unique = true)
    @Enumerated(EnumType.STRING)
    private RoleType name;
    private String Permission;
@ManyToMany(cascade = CascadeType.ALL)
   private List<User> users;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public RoleType getName() {
        return name;
    }

    public void setName(RoleType name) {
        this.name = name;
    }

    public String getPermission() {
        return Permission;
    }

    public void setPermission(String permission) {
        Permission = permission;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }
}