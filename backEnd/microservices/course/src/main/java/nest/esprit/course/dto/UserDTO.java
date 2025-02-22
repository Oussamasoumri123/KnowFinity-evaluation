package nest.esprit.course.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

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
        private LocalDateTime createdAt ;
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

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getFirstName() {
            return firstName;
        }

        public void setFirstName(String firstName) {
            this.firstName = firstName;
        }

        public String getLastName() {
            return lastName;
        }

        public void setLastName(String lastName) {
            this.lastName = lastName;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getBio() {
            return bio;
        }

        public void setBio(String bio) {
            this.bio = bio;
        }

        public Boolean getEnabled() {
            return enabled;
        }

        public void setEnabled(Boolean enabled) {
            this.enabled = enabled;
        }

        public Boolean getNonLocked() {
            return nonLocked;
        }

        public void setNonLocked(Boolean nonLocked) {
            this.nonLocked = nonLocked;
        }

        public Boolean getUsingMfa() {
            return usingMfa;
        }

        public void setUsingMfa(Boolean usingMfa) {
            this.usingMfa = usingMfa;
        }

        public LocalDateTime getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
        }

        public String getImageUrl() {
            return imageUrl;
        }

        public void setImageUrl(String imageUrl) {
            this.imageUrl = imageUrl;
        }
    }
