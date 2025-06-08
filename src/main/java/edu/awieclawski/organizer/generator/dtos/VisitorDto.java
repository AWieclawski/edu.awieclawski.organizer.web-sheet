package edu.awieclawski.organizer.generator.dtos;

import edu.awieclawski.organizer.generator.dtos.base.BaseDto;
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
public class VisitorDto extends BaseDto {
    private String name;
    private String timestamp;
    private String url;
    private String ip;
}
