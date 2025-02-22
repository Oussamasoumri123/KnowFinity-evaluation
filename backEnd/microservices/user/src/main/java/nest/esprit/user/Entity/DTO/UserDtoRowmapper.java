package nest.esprit.user.Entity.DTO;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;
import nest.esprit.user.Entity.Role;
import nest.esprit.user.Entity.User;

@Component
public class UserDtoRowmapper {
    public  static UserDTO fromUser(User user)
    {
        UserDTO userDTO = new UserDTO();
        BeanUtils.copyProperties(user,userDTO);
        return userDTO;
    }
    public  static UserDTO fromUser(User user, Role role)
    {
        UserDTO userDTO = new UserDTO();
        BeanUtils.copyProperties(user,userDTO);
        userDTO.setRoleName(role.getName());
        userDTO.setPermission(role.getPermission());
        return userDTO;
    }
    public  static User toUser(UserDTO userDTO)
    {
        User user = new User();
        BeanUtils.copyProperties(userDTO,user);
        return user;
    }
}
