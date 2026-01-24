package edu.springboot.organizer.web.services;


import edu.springboot.organizer.data.models.Credential;
import edu.springboot.organizer.data.models.User;
import edu.springboot.organizer.data.repositories.UserRepository;
import edu.springboot.organizer.utils.ReflectionUtils;
import edu.springboot.organizer.web.dtos.CredentialDto;
import edu.springboot.organizer.web.dtos.UserDto;
import edu.springboot.organizer.web.dtos.UserSecuredDto;
import edu.springboot.organizer.web.mappers.CredentialRowMapper;
import edu.springboot.organizer.web.mappers.UserSecuredRowMapper;
import edu.springboot.organizer.web.wrappers.SecurityUser;
import edu.springboot.organizer.web.wrappers.UserData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.DependsOn;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Service(value = UserSecuredService.BEAN_NAME)
@DependsOn({UserService.BEAN_NAME, CredentialService.BEAN_NAME})
public class UserSecuredService extends UserService implements UserDetailsService {

    public static final String BEAN_NAME = "edu.springboot.organizer.web.services.UserSecuredService";

    private final CredentialService credentialService;

    public UserSecuredService(UserRepository userRepository,
                              CredentialService credentialService) {
        super(userRepository);
        this.credentialService = credentialService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return Optional.of(getAppUser(username))
                .map(SecurityUser::new)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public UserDto createSecuredUser(UserSecuredDto userDto, CredentialDto credentialDto) {
        assignCredentials(userDto, credentialDto);
        User user = new UserSecuredRowMapper().toEntity(userDto);
        return createUser(user);
    }

    @Transactional(readOnly = true)
    public UserDto getSecuredUserByLogin(String login) {
        UserData userData = findUserByLogin(login);
        return userData.getUserSecured() != null
                ? UserDto.builder()
                .name(userData.getUserSecured().getName())
                .surName(userData.getUserSecured().getSurName())
                .created(userData.getUserSecured().getCreated())
                .hashId(userData.getUserSecured().getHashId())
                .build()
                : null;
    }

    private UserData getAppUser(String username) {
        return findUserByLogin(username);
    }

    private UserData findUserByLogin(String login) {
        UserData userData = UserData.builder().build();
        credentialService.findByLogin(login).ifPresent(cred -> {
                    userData.setCredentialData(new CredentialRowMapper().toDto(cred));
                    Optional<User> optionalUser = super.getRepository().findByCredential(cred.getId());
                    optionalUser.ifPresent(user -> userData.setUserSecured(new UserSecuredRowMapper().toDto(user)));
                }
        );
        return userData;
    }

    private void assignCredentials(UserSecuredDto userDto, CredentialDto credentialDto) {
        if (userDto.getCredentialId() == null && credentialDto.getCreated() == null) {
            CredentialDto credential = credentialService.createCredential(Credential.builder()
                    .email(credentialDto.getEmail())
                    .password(credentialDto.getPassword())
                    .login(credentialDto.getLogin())
                    .build());
            ReflectionUtils.setFieldValue(userDto, "credentialId", credential.getCreated(), true);
        }
    }
}
