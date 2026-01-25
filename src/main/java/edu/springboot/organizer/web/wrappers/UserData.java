package edu.springboot.organizer.web.wrappers;

import edu.springboot.organizer.web.dtos.CredentialDto;
import edu.springboot.organizer.web.dtos.UserDto;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class UserData {
    private UserDto userSecured;
    private CredentialDto credentialData;

    public void validate() {
        this.credentialData.validate();
        this.userSecured.validate();
    }

    public String getErrorMessages() {
        return this.userSecured.getErrorMessage() != null
                ? this.userSecured.getErrorMessage() + (
                this.credentialData.getErrorMessage() != null
                        ? " | " + this.credentialData.getErrorMessage() : "")
                : this.credentialData.getErrorMessage();
    }

}
