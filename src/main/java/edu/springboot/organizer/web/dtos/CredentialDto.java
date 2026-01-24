package edu.springboot.organizer.web.dtos;

import edu.springboot.organizer.web.dtos.base.BaseDto;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Setter
@Getter
@EqualsAndHashCode(callSuper = true, of = {})
@SuperBuilder
public class CredentialDto extends BaseDto {
    private String login;
    private String password;
    private String role;
    private String email;

    @Override
    public void validate() {
    }

    @Override
    public void autoUpdate() {
    }
}
