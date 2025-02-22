package nest.esprit.user.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import nest.esprit.user.Entity.Role;
import nest.esprit.user.Entity.enumeration.RoleType;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

   Role findRoleByName(RoleType name);
   @Query(value = "SELECT r.id, r.name, r.permission " +
           "FROM role r " +
           "JOIN role_users ur ON ur.role_id = r.id " +
           "JOIN Users u ON u.id = ur.users_id " +
           "WHERE u.id = :id",
           nativeQuery = true)
   Role getRoleByUserId(@Param("id") Long id);
   @Query(value = "SELECT r.id, r.name, r.permission " +
           "FROM role r " +
           "JOIN role_users ur ON ur.role_id = r.id " +
           "JOIN Users u ON u.id = ur.users_id " +
           "WHERE u.email = :email",
           nativeQuery = true)
   Role GetRoleByUserEmail(@Param ("email") String email);
}
