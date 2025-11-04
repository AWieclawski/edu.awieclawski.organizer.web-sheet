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
public class EmployeeDto extends BaseDto {
    private String surName;
    private String uniqNick;
    private String userId;
    private String name;

    public String getFullName() {
        String name = this.name != null ? this.name : "";
        String surName = this.surName != null ? this.surName : "";
        return (!name.isEmpty() ? name + " " : "") + surName;
    }

    public static String getDefaultName() {
        return "Input the name";
    }

    @Override
    public void validate() {
    }

    @Override
    public void autoUpdate() {
    }
}
