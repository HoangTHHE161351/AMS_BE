package vn.attendance.config.authen;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import vn.attendance.config.authen.entity.UserPrincipal;
import vn.attendance.model.Role;
import vn.attendance.model.Users;
import vn.attendance.repository.RoleRepository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class BaseUserDetailsService implements UserDetailsService {
    @Autowired
    RoleRepository roleRepository;
    public static final ThreadLocal<Users> USER = new ThreadLocal<>();

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        Users user = Optional.ofNullable(USER.get())
            .orElseThrow(() -> new UsernameNotFoundException("Email " + userName + " not found"));
        USER.set(user);
        UserDetails userDetails = null;
        Role role = roleRepository.findById(user.getRoleId()).orElseThrow(() -> new UsernameNotFoundException("None"));
        try {
            userDetails = UserPrincipal.create(user, getAuthorities(role),role);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return userDetails;

    }

    public static Collection<? extends GrantedAuthority> getAuthorities(Role role) {
        String[] userRoles = List.of(role).stream().map(Role::getRoleName)
                .toArray(String[]::new);
        return AuthorityUtils.createAuthorityList(userRoles);
    }
}
