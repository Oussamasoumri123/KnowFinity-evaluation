package nest.esprit.user.Service.ServiceImpl;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import nest.esprit.user.Entity.*;
import nest.esprit.user.Repository.*;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.keygen.KeyGenerators;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import nest.esprit.user.Configuration.UserPrincipal;
import nest.esprit.user.Entity.DTO.UserDTO;
import nest.esprit.user.Entity.DTO.UserDtoRowmapper;
import nest.esprit.user.Entity.enumeration.RoleType;
import nest.esprit.user.Exception.ApiException;
import nest.esprit.user.Service.RoleService;
import nest.esprit.user.Service.UserService;


import java.beans.PropertyDescriptor;
import java.util.Arrays;
import java.util.Date;

import java.util.List;
import java.util.UUID;





import static nest.esprit.user.Entity.enumeration.VerificationType.ACCOUNT;
import static nest.esprit.user.Entity.enumeration.VerificationType.PASSWORD;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService , UserDetailsService {
    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
    private  String DATE_FORMAT= "yyyy-MM-dd hh:mm:ss";
    @Autowired
    TwoFactorVerifRepository twoFactorVerifRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    RoleService roleService;
    @Autowired
    AccountVerificationsRepository accountVerificationsRepository;
    @Autowired
    private final BCryptPasswordEncoder encoder ;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private UserDtoRowmapper userDtoRowmapper;
    @Autowired
    private ResetPasswordRepository resetPasswordRepository;

    @Autowired
    public UserServiceImpl(
            UserRepository userRepository,
            RoleService roleService,
            AccountVerificationsRepository accountVerificationsRepository,
            BCryptPasswordEncoder encoder,
            RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleService = roleService;
        this.accountVerificationsRepository = accountVerificationsRepository;
        this.encoder = encoder;
        this.roleRepository = roleRepository;
    }

    @Override
    public UserDTO createUser(User user) {

        if(userRepository.countByEmail(user.getEmail().trim().toLowerCase()) > 0){
            throw new ApiException("Email already in use");
        }
try {
    user.setEnabled(false);
    user.setNonLocked(true);
    user.setPassword(encoder.encode(user.getPassword()));
    userRepository.saveAndFlush(user);
    roleService.addRoleToUser(user.getId(), RoleType.ROLE_STUDENT); //this is gonna be changed on preferences of the user role
    String verificationUrl = getVerificationUrl(UUID.randomUUID().toString(), ACCOUNT.getType());
    AccountVerifications accountVerifications = new AccountVerifications();
    accountVerifications.setUser(user);
    accountVerifications.setUrl(verificationUrl);
    accountVerificationsRepository.saveAndFlush(accountVerifications);
    logger.info("this the verification Email {} has been created", verificationUrl);
    //send verification mail
    return mapToUserDTO(user);
}
catch (EmptyResultDataAccessException e){
    throw new ApiException("No Role found by" + RoleType.ROLE_STUDENT.name());
}
catch (Exception e){
    throw new ApiException(e.getMessage()+"an error occurred try again");
}
    }

    @Override
    public UserDTO getUserByEmail(String email) { //surround block try catch if not exist throw Http Responce not found in the database

        User user = userRepository.findByEmail(email.trim().toLowerCase());
        return mapToUserDTO(user);
    }

    @Override
    public void sendVerifCode(UserDTO userDTO) {
        // Fetch the User entity from the database if it exists
        User user = userRepository.findById(userDTO.getId())
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + userDTO.getId()));

        // Generate verification code
        String verificationCode = KeyGenerators.string().generateKey().substring(0, 8).toUpperCase();

        // Check if an existing TwoFactorVerif record exists for the user
        TwoFactorVerif existingVerif = twoFactorVerifRepository.getTwoFactorVerifByUserEmail(user.getEmail());
        if (existingVerif!=null) {
            // Update the existing record

            existingVerif.setCode(verificationCode);

            // Set new expiration date
            Date now = new Date();
            Date nextHalfHour = DateUtils.addMinutes(now, 30);
            existingVerif.setExpirationDate(nextHalfHour);

            // Save the updated record
            twoFactorVerifRepository.save(existingVerif);
        } else {
            // Create a new TwoFactorVerif record
            TwoFactorVerif newVerif = new TwoFactorVerif();
            newVerif.setCode(verificationCode);
            newVerif.setUser(user); // Use managed User entity

            // Set expiration date
            Date now = new Date();
            Date nextHalfHour = DateUtils.addMinutes(now, 30);
            newVerif.setExpirationDate(nextHalfHour);

            // Save the new record
            twoFactorVerifRepository.save(newVerif);
        }

        // Send SMS (commented for testing purposes)
        // sendSMS("+216" + userDTO.getPhone(), "From Application \n Verification Code \n" + verificationCode);

        logger.info("This is the sent code: {}", verificationCode);
    }


    @Override
    public UserDTO verifyCode(String email, String code) {
        try {
            // Fetch the TwoFactorVerif entity by user email
            TwoFactorVerif twoFactorVerif = twoFactorVerifRepository.getTwoFactorVerifByUserEmail(email);

            // Check if the code is expired
            if (twoFactorVerif.getExpirationDate().before(new Date())) {
                throw new ApiException("Expired code,login again");
            }

            // Check if the provided code matches
            if (!twoFactorVerif.getCode().equals(code)) {
                throw new ApiException("Wrong verification code");
            }

            // Sever the relationship between User and TwoFactorVerif
            User user = twoFactorVerif.getUser();
            user.setTwoFactorVerif(null); // Remove the association
            userRepository.save(user); // Save the updated User entity

            // Safely delete the TwoFactorVerif entity
            twoFactorVerifRepository.delete(twoFactorVerif);

            // Return the UserDTO
            return mapToUserDTO(user);

        } catch (EmptyResultDataAccessException e) {
            throw new ApiException("Could not find record");
        }
    }

    @Override
    public UserDTO verifyPassword(String key) {
        ResetPassword resetPassword = resetPasswordRepository.findByUrl( getVerificationUrl(key, PASSWORD.getType()) );
        if (resetPassword == null) {
            throw new ApiException("Invalid verification URL,please reset ur password again  ");
        }
        if (resetPassword.getExpirationDate().before(new Date())) {
            throw new ApiException("Expired code,login again");
        }
        else
        { resetPasswordRepository.delete(resetPassword);
        return UserDtoRowmapper.fromUser(resetPasswordRepository.findUserByUrl(getVerificationUrl(key, PASSWORD.getType())));}
    }

    @Override
    public void renewPassword(String key, String password, String confirmPassword) {
        ResetPassword resetPassword = resetPasswordRepository.findByUrl( getVerificationUrl(key, PASSWORD.getType()) );
       User user= resetPasswordRepository.findUserByUrl(getVerificationUrl(key, PASSWORD.getType()));
        if(!password.equals(confirmPassword)) throw new ApiException("Passwords do not match");
        if (resetPassword == null) {
            throw new ApiException("Invalid verification URL,please reset ur password again");
        }
        if (resetPassword.getExpirationDate().before(new Date())) {
            throw new ApiException("Expired code,login again");
        }
        else
        { user.setPassword(encoder.encode(password));
        userRepository.save(user);
        }
        }

    @Override
    public UserDTO verifyAccount(String key) {
        AccountVerifications accountVerifications = accountVerificationsRepository.findByUrl(getVerificationUrl(key,ACCOUNT.getType()));

        if (accountVerifications == null) {
            throw new ApiException("un existed account verification,try to verify again");
        }
        else
        {     User user = accountVerifications.getUser();
            user.setEnabled(true);
            userRepository.save(user);
            accountVerificationsRepository.delete(accountVerifications);
            return UserDtoRowmapper.fromUser(user);
        }
    }

    @Override
    public UserDTO createTutor(User user) {

        if(userRepository.countByEmail(user.getEmail().trim().toLowerCase()) > 0){
            throw new ApiException("Email already in use");
        }
        try {
            user.setEnabled(true);
            user.setNonLocked(true);
            user.setPassword(encoder.encode(user.getPassword()));
            userRepository.saveAndFlush(user);
            roleService.addRoleToUser(user.getId(), RoleType.ROLE_TUTOR);
            return mapToUserDTO(user);
        }
        catch (EmptyResultDataAccessException e){
            throw new ApiException("No Role found by" + RoleType.ROLE_TUTOR.name());
        }
        catch (Exception e){
            throw new ApiException(e.getMessage()+"an error occurred try again");
        }
    }

    @Override
    public UserDTO createAdmin(User user) {
        if(userRepository.countByEmail(user.getEmail().trim().toLowerCase()) > 0){
            throw new ApiException("Email already in use");
        }
        try {
            user.setEnabled(true);
            user.setNonLocked(true);
            user.setPassword(encoder.encode(user.getPassword()));
            userRepository.saveAndFlush(user);
            roleService.addRoleToUser(user.getId(), RoleType.ROLE_ADMIN);
            return mapToUserDTO(user);
        }
        catch (EmptyResultDataAccessException e){
            throw new ApiException("No Role found by" + RoleType.ROLE_ADMIN.name());
        }
        catch (Exception e){
            throw new ApiException(e.getMessage()+"an error occurred try again");
        }
    }

    public void copyNonNullProperties(User source, User target) {
        BeanUtils.copyProperties(source, target, getNullPropertyNames(source));
    }

    private String[] getNullPropertyNames(Object source) {
        final BeanWrapper wrappedSource = new BeanWrapperImpl(source);
        return Arrays.stream(wrappedSource.getPropertyDescriptors())
                .map(PropertyDescriptor::getName)
                .filter(name -> wrappedSource.getPropertyValue(name) == null)
                .toArray(String[]::new);
    }

    @Override
    public UserDTO updateUser(User user) {
        if (user.getPassword() != null) {
            user.setPassword(encoder.encode(user.getPassword()));
        }
        User existingUser = userRepository.findById(user.getId()).orElseThrow(
                () -> new EntityNotFoundException("User not found")
        );
        if (user.getPassword() != null) {
            existingUser.setPassword(encoder.encode(user.getPassword()));
        }
        user.setEnabled(true);
        copyNonNullProperties(user, existingUser);

        return mapToUserDTO(userRepository.save(existingUser));

    }

    @Override
    @Transactional
    public void deleteUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        // First, remove the user from each role's users list
        for (Role role : user.getRole()) {
            role.getUsers().remove(user);  // Remove user from role
        }

        // Now, clear user's role list to remove the references in the join table
        user.getRole().clear();
        userRepository.save(user); // Save changes to remove associations in the join table

        // Finally, delete the user
        userRepository.delete(user);
    }

    @Override
    public List<User> getUsers() {

 return userRepository.findAll();  }



    static String getVerificationUrl (String key, String type) {
return ServletUriComponentsBuilder.fromCurrentContextPath().path("/user/verify/"+type+"/"+key).toUriString();
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email.trim().toLowerCase());
        if(user == null){
            logger.info("user not found");
            throw new UsernameNotFoundException("user not found in the database");

        }
        else {
            logger.info("user found in the database");
        }
        return new UserPrincipal(user,roleService.GetRoleByUserId(user.getId()));


    }
    private UserDTO mapToUserDTO(User user) {
        return UserDtoRowmapper.fromUser(user,roleService.GetRoleByUserId(user.getId()));
    }
}
