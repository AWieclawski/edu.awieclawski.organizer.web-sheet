package edu.springboot.organizer.web.wrappers;

import edu.springboot.organizer.web.dtos.CredentialDto;
import edu.springboot.organizer.web.dtos.UserSecuredDto;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class UserData {
    private UserSecuredDto userSecured;
    private CredentialDto credentialData;
}
