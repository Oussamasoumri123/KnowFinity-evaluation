package nest.esprit.apigateway.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import nest.esprit.apigateway.dto.RoleType;

import java.time.LocalDateTime;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDTO {

    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String address;
    private String phone;
    private String title;
    private String bio;
    private Boolean enabled = false;
    private Boolean nonLocked = true;
    private Boolean usingMfa = false;
    private LocalDateTime createdAt;
    private String imageUrl;
    private RoleType roleName;
    private String permission;

    public UserDTO() {
    }

    public UserDTO(String permission, RoleType roleName, String imageUrl, LocalDateTime createdAt, Boolean usingMfa, Boolean nonLocked, Boolean enabled, String bio, String title, String phone, String address, String email, String lastName, String firstName, Long id) {
        this.permission = permission;
        this.roleName = roleName;
        this.imageUrl = imageUrl;
        this.createdAt = createdAt;
        this.usingMfa = usingMfa;
        this.nonLocked = nonLocked;
        this.enabled = enabled;
        this.bio = bio;
        this.title = title;
        this.phone = phone;
        this.address = address;
        this.email = email;
        this.lastName = lastName;
        this.firstName = firstName;
        this.id = id;
    }
}
