package edu.springboot.organizer.web.wrappers;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

@RequiredArgsConstructor
public class SecurityUser implements UserDetails {

    private final UserData appUser;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        String role = appUser.getCredentialData().getRole();
        String prefixedRole = role.startsWith("ROLE_") ? role : "ROLE_" + role;
        return Collections.singleton(new SimpleGrantedAuthority(prefixedRole));
    }

    @Override
    public String getPassword() {
        return appUser.getCredentialData().getPassword();
    }

    @Override
    public String getUsername() {
        return appUser.getCredentialData().getLogin();
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
        return true;
    }

}
