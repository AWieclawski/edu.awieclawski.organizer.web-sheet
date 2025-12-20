package edu.springboot.organizer.web.dtos;

import edu.springboot.organizer.web.dtos.base.BaseDto;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Setter
@Getter
@EqualsAndHashCode(callSuper = true, of = {})
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class UserDto extends BaseDto {
    private String name;
    private String surName;

    @Override
    public void validate() {
    }

    @Override
    public void autoUpdate() {
    }
}
