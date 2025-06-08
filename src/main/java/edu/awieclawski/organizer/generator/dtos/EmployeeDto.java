package edu.awieclawski.organizer.generator.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@AllArgsConstructor
@RequiredArgsConstructor
@Builder
@Getter
public class EmployeeDto {
    private String name;
    private String surName;
    private String uniqNick;
    private Integer hashId;
}
