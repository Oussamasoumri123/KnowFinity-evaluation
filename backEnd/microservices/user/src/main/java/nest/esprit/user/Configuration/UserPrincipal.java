package nest.esprit.user.Configuration;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import nest.esprit.user.Entity.DTO.UserDTO;
import nest.esprit.user.Entity.Role;
import nest.esprit.user.Entity.User;

import java.util.Collection;

import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toList;
import static nest.esprit.user.Entity.DTO.UserDtoRowmapper.fromUser;


public class UserPrincipal implements UserDetails {
    private final User user;
    private final Role role;
    public UserPrincipal(User user, Role role) {
        this.user = user;
        this.role = role;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return stream(this.role.getPermission().split(",".trim())).map(SimpleGrantedAuthority::new).collect(toList());
    }

    @Override
    public String getPassword() {
        return this.user.getPassword();
    }

    @Override
    public String getUsername() {
        return this.user.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return this.user.getNonLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return this.user.getEnabled();
    }
    public UserDTO getUser(){
        return fromUser(this.user,role);
    }
}
