package nest.esprit.user.Service.ServiceImpl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import nest.esprit.user.Entity.Role;
import nest.esprit.user.Entity.User;
import nest.esprit.user.Entity.enumeration.RoleType;
import nest.esprit.user.Exception.ApiException;
import nest.esprit.user.Repository.RoleRepository;
import nest.esprit.user.Repository.UserRepository;
import nest.esprit.user.Service.RoleService;

import static nest.esprit.user.Entity.enumeration.RoleType.ROLE_STUDENT;


@Service
public class RoleServiceImpl implements RoleService {
    @Autowired
    UserRepository userRepository;
    @Autowired
    RoleRepository roleRepository;
    private static final Logger logger = LoggerFactory.getLogger(RoleServiceImpl.class);
    @Override
    public void addRoleToUser(Long userId, RoleType roleName) {

        logger.info("adding Role {} to user {}", roleName, userId);
        try{
            User user = userRepository.findById(userId).get();
            Role role = roleRepository.findRoleByName(roleName);
            role.getUsers().add(user);
            roleRepository.save(role);
        }

        catch (EmptyResultDataAccessException e){
            throw new ApiException("No Role found by" + ROLE_STUDENT.name());
        }
        catch (Exception e){
            throw new ApiException(e.getMessage()+"an error occurred try again");
        }


    }

    @Override
    public Role GetRoleByUserId(Long userId) {
        return roleRepository.getRoleByUserId(userId);
    }

    @Override
    public Role GetRoleByUserEmail(String email) {
        return roleRepository.GetRoleByUserEmail(email);
    }

    @Override
    public void UpdateUserRole(Long userId, String roleName) {

    }
}
