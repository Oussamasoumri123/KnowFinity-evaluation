package nest.esprit.user.Service;

import nest.esprit.user.Entity.Role;
import nest.esprit.user.Entity.enumeration.RoleType;

public interface RoleService {
void addRoleToUser(Long userId, RoleType roleType);
Role GetRoleByUserId(Long userId);
Role GetRoleByUserEmail(String email);
void UpdateUserRole(Long userId,String roleName);
}
