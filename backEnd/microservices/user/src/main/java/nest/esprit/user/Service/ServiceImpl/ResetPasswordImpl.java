package nest.esprit.user.Service.ServiceImpl;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import nest.esprit.user.Entity.ResetPassword;
import nest.esprit.user.Entity.User;
import nest.esprit.user.Exception.ApiException;
import nest.esprit.user.Repository.ResetPasswordRepository;
import nest.esprit.user.Repository.UserRepository;
import nest.esprit.user.Service.ResetPasswordService;

import java.util.Date;
import java.util.UUID;

import static nest.esprit.user.Entity.enumeration.VerificationType.PASSWORD;
import static nest.esprit.user.Service.ServiceImpl.UserServiceImpl.getVerificationUrl;
@Slf4j
@Service
public class ResetPasswordImpl implements ResetPasswordService {
    @Autowired
    ResetPasswordRepository resetPasswordRepository;
    @Autowired
    UserRepository userRepository;

    @Override
    public void resetPassword(String email) {
        // Fetch the user by email
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new ApiException("User not found");
        }

        try {
            // Check if the user already has a ResetPassword entity
            ResetPassword resetPassword = user.getResetPassword();

            // Generate a new URL and expiration date
            Date now = new Date();
            Date nextDay = DateUtils.addDays(now, 1);
            String verificationURL = getVerificationUrl(UUID.randomUUID().toString(), PASSWORD.getType());
            log.info("verificationURL: {}", verificationURL);

            if (resetPassword == null) {
                // Create a new ResetPassword record if none exists
                resetPassword = new ResetPassword();
                resetPassword.setUser(user); // Associate the managed User entity
            }

            // Update the URL and expiration date
            resetPassword.setUrl(verificationURL);
            resetPassword.setExpirationDate(nextDay);

            // Save the ResetPassword entity
            resetPasswordRepository.save(resetPassword);
            log.info("Reset password sent to email {}", email);

        } catch (Exception e) {
            throw new ApiException("Failed to reset password: " + e.getMessage());
        }

    }


}
