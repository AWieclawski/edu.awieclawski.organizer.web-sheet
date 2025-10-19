package edu.springboot.organizer.web.dtos;

import edu.springboot.organizer.web.dtos.base.BaseDto;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@EqualsAndHashCode(callSuper = true, of = {})
public class EmployeeDto extends BaseDto {
    private final String surName;
    private final String uniqNick;
    private final String userId;
    @Setter
    private String name;

    @Builder
    public EmployeeDto(String created, Integer hashId, String name, String surName, String uniqNick, String userId) {
        super(created, hashId);
        this.name = name;
        this.surName = surName;
        this.uniqNick = uniqNick;
        this.userId = userId;
    }

    public String getFullName() {
        String name = this.name != null ? this.name : "";
        String surName = this.surName != null ? this.surName : "";
        return (!name.isEmpty() ? name + " " : "") + surName;
    }

    public static String getDefaultName() {
        return "Input the name";
    }
}
