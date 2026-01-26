package edu.springboot.organizer.web.dtos;

import edu.springboot.organizer.web.dtos.base.BaseDto;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.regex.Pattern;

@Setter
@Getter
@EqualsAndHashCode(callSuper = true, of = {})
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class CredentialDto extends BaseDto {
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*" +
            "@[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$", Pattern.CASE_INSENSITIVE);

    private String login;
    private String password;
    private Role role;
    private String email;

    @Override
    public void validate() {
        validateLogin();
        validatePassword();
        validateRole();
        validateEmail();
    }

    @Override
    public void autoUpdate() {
    }

    public void addErrorMessage(String message) {
        handleErrorMessage(message);
    }

    // validators

    private void validateLogin() {
        if (this.login == null)
            handleErrorMessage("Login cannot be empty!");
    }

    private void validatePassword() {
        if (this.password == null)
            handleErrorMessage("Password cannot be empty!");
    }

    private void validateRole() {
        if (this.role == null)
            handleErrorMessage("Role cannot be empty!");
    }

    private void validateEmail() {
        if (this.email == null || this.email.isEmpty()) {
            handleErrorMessage("Email cannot be empty!");
        } else {
            if (!EMAIL_PATTERN.matcher(this.email).matches()) {
                handleErrorMessage("Email not valid! [" + this.email + "]");
            }
        }
    }

}
