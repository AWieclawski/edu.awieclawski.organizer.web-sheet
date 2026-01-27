package edu.springboot.organizer.web.services;


import edu.springboot.organizer.data.models.Credential;
import edu.springboot.organizer.data.models.User;
import edu.springboot.organizer.web.dtos.CredentialDto;
import edu.springboot.organizer.web.dtos.UserDto;
import edu.springboot.organizer.web.mappers.CredentialRowMapper;
import edu.springboot.organizer.web.mappers.UserRowMapper;
import edu.springboot.organizer.web.wrappers.SecurityUser;
import edu.springboot.organizer.web.wrappers.UserData;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.DependsOn;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.function.Consumer;

/**
 * <a href="https://medium.com/@AlexanderObregon/building-a-login-system-with-spring-boot-and-spring-security-2ef6f110a9cb">
 * Building a Login System with Spring Boot and Spring Security</a>
 */

@Slf4j
@Service(value = UserSecuredService.BEAN_NAME)
@DependsOn({UserService.BEAN_NAME, CredentialService.BEAN_NAME})
@RequiredArgsConstructor
public class UserSecuredService implements UserDetailsService {

    public static final String BEAN_NAME = "edu.springboot.organizer.web.services.UserSecuredService";

    private final CredentialService credentialService;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return getOptionalAppUserByLogin(username)
                .map(SecurityUser::new)
                .orElseThrow(() -> new UsernameNotFoundException("User " + username + " not found!"));
    }

    @Transactional(readOnly = true)
    public void checkUserData(final UserData userData) {
        credentialService.findByLogin(userData.getCredentialData().getLogin()).ifPresent(
                it -> userData.getCredentialData()
                        .addErrorMessage("Login already taken: " + it.getLogin()));
        credentialService.findByEmail(userData.getCredentialData().getEmail()).ifPresent(
                it -> userData.getCredentialData()
                        .addErrorMessage("Email already taken: " + it.getEmail()));
    }

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public UserDto createSecuredUser(UserData userData) {
        return creeateUserDto(userData);
    }

    private UserDto creeateUserDto(UserData userData) {
        assignCredentials(userData); // incl. password encoding
        User user = new UserRowMapper().toEntity(userData.getUserSecured());
        return userService.createUser(user);
    }

    private Optional<UserData> getOptionalAppUserByLogin(String login) {
        return Optional.ofNullable(getUserDataByLogin(login));
    }

    private UserData getUserDataByLogin(String login) {
        UserData userData = UserData.builder().build();
        credentialService.findByLogin(login).ifPresent(credential -> userService.getRepository().findByCredential(credential.getId())
                .ifPresent(getUserConsumer(credential, userData))
        );
        return userData;
    }

    private static Consumer<User> getUserConsumer(Credential credential, UserData userData) {
        return user -> {
            userData.setCredentialData(new CredentialRowMapper().toDto(credential));
            userData.setUserSecured(new UserRowMapper().toDto(user));
        };
    }

    private void assignCredentials(UserData userData) {
        if (userData.getUserSecured().getCredentialId() == null && userData.getCredentialData().getCreated() == null) {
            userData.getCredentialData().setPassword(passwordEncoder.encode(userData.getCredentialData().getPassword()));
            CredentialDto credential = credentialService.createCredential(new CredentialRowMapper().toEntity(userData.getCredentialData()));
            userData.getUserSecured().setCredentialId(credential.getCreated());
        }
    }

}
