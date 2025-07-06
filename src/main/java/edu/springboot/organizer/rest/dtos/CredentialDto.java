package edu.springboot.organizer.rest.dtos;

import edu.springboot.organizer.rest.dtos.base.BaseDto;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@EqualsAndHashCode(callSuper = true, of = {})
public class CredentialDto extends BaseDto {
    private final String login;
    private final String password;
    @Setter
    private String email;

    @Builder
    public CredentialDto(String created, Integer hashId, String login, String password, String email) {
        super(created, hashId);
        this.login = login;
        this.password = password;
        this.email = email;
    }
}
