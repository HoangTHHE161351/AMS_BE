package vn.attendance.config.authen.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import vn.attendance.model.Role;
import vn.attendance.model.Users;
import vn.attendance.util.Constants;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

/**
 * User details.
 */
@Getter
@NoArgsConstructor
public class UserPrincipal implements UserDetails {

    private Integer userId;
    private String name;
    @JsonIgnore
    private String password;
    //	private String langKey;
    private boolean tfaChecked;
    private List<Role> roles;
    private String status;
	private Collection<? extends GrantedAuthority> authorities;

    public UserPrincipal(Integer id, String password,
                         String status, List<Role> roles, Collection<? extends GrantedAuthority> authorities) {
        this.userId = id;
//        this.name = name;
//		this.langKey = langKey;
        this.password = password;
        this.status = status;
        this.roles = roles;
        this.authorities = authorities;
    }

    public UserPrincipal(Integer id, String password,
                         String status, List<Role> roles) {
        this.userId = id;
//        this.name = name;
//		this.langKey = langKey;
        this.password = password;
        this.status = status;
        this.roles = roles;

    }

    public static UserPrincipal create(Users user, Role role, Collection<? extends GrantedAuthority> authorities) throws UsernameNotFoundException, Exception {
        if (Objects.isNull(user)) {
            throw new UsernameNotFoundException("The user is empty");
        }

        return new UserPrincipal(user.getId(), user.getPassword(), user.getStatus(), Arrays.asList(role), authorities);
    }

    public static UserPrincipal create(Users user,Collection<? extends GrantedAuthority> authorities, Role role) throws UsernameNotFoundException, Exception {
        if (Objects.isNull(user)) {
            throw new UsernameNotFoundException("The user is empty");
        }

        return new UserPrincipal(user.getId(), user.getPassword(),  user.getStatus(),List.of(role));
    }




    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return name;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

	@Override
	public boolean isEnabled() {

		return status.equals(Constants.STATUS_TYPE.ACTIVE);
	}
}
