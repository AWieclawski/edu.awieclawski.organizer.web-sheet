package edu.springboot.organizer.generator.dtos;

import edu.springboot.organizer.generator.dtos.base.BaseDto;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@EqualsAndHashCode(callSuper = true, of = {})
public class EmployeeDto extends BaseDto {
    private final String surName;
    private final String uniqNick;
    @Setter
    private String name;

    @Builder
    public EmployeeDto(String created, Integer hashId, String name, String surName, String uniqNick) {
        super(created, hashId);
        this.name = name;
        this.surName = surName;
        this.uniqNick = uniqNick;
    }
}
