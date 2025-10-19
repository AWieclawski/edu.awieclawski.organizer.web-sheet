package edu.springboot.organizer.web.dtos;

import edu.springboot.organizer.web.dtos.base.BaseDto;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode(callSuper = true, of = {})
public class UserSecuredDto extends BaseDto {
    private final String name;
    private final String surName;
    private final String credentialId;

    @Builder
    public UserSecuredDto(String created, Integer hashId, String name, String surName, String credentialId) {
        super(created, hashId);
        this.name = name;
        this.surName = surName;
        this.credentialId = credentialId;
    }
}
