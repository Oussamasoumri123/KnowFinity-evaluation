package nest.esprit.user.Controller;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data   
public class LoginForm {
    @NotEmpty(message = "cannot be empty")
    @Email(message = "Invalid email, please enter a valid Email")
    private String email;
    @NotEmpty(message = "Password cannot be empty")
    private String password;

    public @NotEmpty String getEmail() {
        return email;
    }

    public void setEmail(@NotEmpty String email) {
        this.email = email;
    }

    public @NotEmpty String getPassword() {
        return password;
    }

    public void setPassword(@NotEmpty String password) {
        this.password = password;
    }
}
