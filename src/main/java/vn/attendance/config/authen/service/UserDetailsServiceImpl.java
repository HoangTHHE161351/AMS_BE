package vn.attendance.config.authen.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import vn.attendance.config.authen.BaseUserDetailsService;
import vn.attendance.exception.AmsException;
import vn.attendance.model.Role;
import vn.attendance.repository.RoleRepository;
import vn.attendance.repository.UsersRepository;
import vn.attendance.util.MessageCode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired(required = true)
    UsersRepository usersRepository;
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    private BCryptPasswordEncoder encoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        // hard coding the users. All passwords must be encoded.
        final List<vn.attendance.model.Users> users = Arrays.asList(usersRepository.findByUsername(username).get());
        BaseUserDetailsService.USER.set(users.get(0));
        for (vn.attendance.model.Users appUser : users) {
            if (appUser.getUsername().equals(username)) {
                Role role;
                try {
                    role = roleRepository.findById(appUser.getRoleId()).orElseThrow(() -> new AmsException(MessageCode.ROLE_NOT_FOUND));
                } catch (AmsException e) {
                    throw new RuntimeException(e);
                }
                List<String> roles = new ArrayList<>(Arrays.asList(role.getRoleName()));
                List<GrantedAuthority> grantedAuthorities = AuthorityUtils
                        .commaSeparatedStringToAuthorityList(roles.toString().replace("[", "").replace("]", ""));
                return new User(appUser.getUsername(), appUser.getPassword(), grantedAuthorities);
            }
        }
        // If user not found. Throw this exception.
        throw new UsernameNotFoundException("Username: " + username + " not found!");
        }
}
