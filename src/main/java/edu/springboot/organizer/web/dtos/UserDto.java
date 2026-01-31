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
    private String credentialId;

    @Override
    public void validate() {
        validateName();
        validateSurname();
    }

    @Override
    public void autoUpdate() {
    }

    private void validateName() {
        if (this.name == null || this.name.isEmpty())
            handleErrorMessage("Name cannot be empty!");
    }

    private void validateSurname() {
        if (this.surName == null || this.surName.isEmpty())
            handleErrorMessage("Surname cannot be empty!");
    }
}
