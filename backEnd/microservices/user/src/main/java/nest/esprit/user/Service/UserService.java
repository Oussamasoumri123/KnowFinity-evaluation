package nest.esprit.user.Service;

import nest.esprit.user.Entity.DTO.UserDTO;
import nest.esprit.user.Entity.User;

public interface UserService {
    UserDTO createUser(User user);
    UserDTO getUserByEmail(String email);
    void sendVerifCode(UserDTO user);
    UserDTO verifyCode(String email, String code);

    UserDTO verifyPassword(String key);

    void renewPassword(String key, String password, String confirmPassword);

    UserDTO verifyAccount(String key);
    UserDTO createTutor(User user);
    UserDTO createAdmin(User user);
    UserDTO updateUser(User user);
    void deleteUser(Long id);

}
