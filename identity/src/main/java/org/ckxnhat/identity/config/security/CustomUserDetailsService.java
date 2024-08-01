package org.ckxnhat.identity.config.security;

import lombok.RequiredArgsConstructor;
import org.ckxnhat.identity.repository.UserRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.ArrayList;
import java.util.List;

/**
 * @author MinhNhat
 * @email nguyennhat.110120@gmail.com
 * @datetime 2024-07-24 16:09:44.028
 */

@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        org.ckxnhat.identity.model.User user = userRepository
                .findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Email không tồn tại"));
        if(user.getRole() == null){
            throw new UsernameNotFoundException("Role không tồn tại");
        }
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(user.getRole().getName()));
        return new CustomUserDetails(user.getEmail(), user.getPassword(), authorities, user.getId());
    }
}
