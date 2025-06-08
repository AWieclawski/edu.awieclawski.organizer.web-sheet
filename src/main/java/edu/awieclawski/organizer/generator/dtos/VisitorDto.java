package edu.awieclawski.organizer.generator.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@AllArgsConstructor
@RequiredArgsConstructor
@Builder
@Getter
public class VisitorDto {
    private String name;
    private String timestamp;
    private String url;
    private String ip;
    private Integer hashId;
}
