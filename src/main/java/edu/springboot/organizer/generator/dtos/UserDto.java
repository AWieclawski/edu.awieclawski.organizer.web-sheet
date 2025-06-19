package edu.springboot.organizer.generator.dtos;

import edu.springboot.organizer.generator.dtos.base.BaseDto;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.SuperBuilder;

@Getter
@AllArgsConstructor
@RequiredArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper = true, of = {})
public class UserDto extends BaseDto {
    private String name;
    private String surName;
    private String email;
    private String login;
    private String password;
}
