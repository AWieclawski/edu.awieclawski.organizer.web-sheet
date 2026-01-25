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
import java.util.concurrent.atomic.AtomicReference;
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
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return getOptionalAppUserByName(username)
                .map(SecurityUser::new)
                .orElseThrow(() -> new UsernameNotFoundException("User " + username + " not found!"));
    }

    public UserDetails checkUserByCredentials(CredentialDto credentialDto) {
        return getOptionalAppUserByName(credentialDto.getLogin())
                .map(SecurityUser::new)
                .orElse(getOptionalAppUserByEmail(credentialDto.getEmail())
                        .map(SecurityUser::new)
                        .orElse(null)
                );
    }

    public UserDetails getUserByUsername(String username) {
        return getOptionalAppUserByName(username)
                .map(SecurityUser::new)
                .orElse(null);
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

    private Optional<UserData> getOptionalAppUserByName(String username) {
        return Optional.ofNullable(findAtomicUserByLogin(username).get());
    }

    private Optional<UserData> getOptionalAppUserByEmail(String username) {
        return Optional.ofNullable(findAtomicUserByEmail(username).get());
    }

    private AtomicReference<UserData> findAtomicUserByLogin(String login) {
        AtomicReference<UserData> userDataAtomicReference = new AtomicReference<>();
        credentialService.findByLogin(login).ifPresent(credential -> userService.getRepository().findByCredential(credential.getId())
                .ifPresent(getUserConsumer(credential, userDataAtomicReference))
        );
        return userDataAtomicReference;
    }

    private AtomicReference<UserData> findAtomicUserByEmail(String email) {
        AtomicReference<UserData> userDataAtomicReference = new AtomicReference<>();
        credentialService.findByEmail(email).ifPresent(credential -> userService.getRepository().findByCredential(credential.getId())
                .ifPresent(getUserConsumer(credential, userDataAtomicReference))
        );
        return userDataAtomicReference;
    }

    private static Consumer<User> getUserConsumer(Credential credential, AtomicReference<UserData> userDataAtomicReference) {
        return user -> userDataAtomicReference.lazySet(
                UserData.builder()
                        .credentialData(new CredentialRowMapper().toDto(credential))
                        .userSecured(new UserRowMapper().toDto(user))
                        .build()
        );
    }

    private void assignCredentials(UserData userData) {
        if (userData.getUserSecured().getCredentialId() == null && userData.getCredentialData().getCreated() == null) {
            userData.getCredentialData().setPassword(passwordEncoder.encode(userData.getCredentialData().getPassword()));
            CredentialDto credential = credentialService.createCredential(new CredentialRowMapper().toEntity(userData.getCredentialData()));
            userData.getUserSecured().setCredentialId(credential.getCreated());
        }
    }

}
