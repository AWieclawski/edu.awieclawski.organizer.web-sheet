package edu.springboot.organizer.rest.dtos;

import edu.springboot.organizer.rest.dtos.base.BaseDto;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode(callSuper = true, of = {})
public class UserDto extends BaseDto {
    private final String name;
    private final String surName;

    @Builder
    public UserDto(String created, Integer hashId, String name, String surName) {
        super(created, hashId);
        this.name = name;
        this.surName = surName;
    }
}
