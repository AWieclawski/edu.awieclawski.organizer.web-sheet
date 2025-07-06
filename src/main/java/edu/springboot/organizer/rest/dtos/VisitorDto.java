package edu.springboot.organizer.rest.dtos;

import edu.springboot.organizer.rest.dtos.base.BaseDto;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode(callSuper = true, of = {})
public class VisitorDto extends BaseDto {
    private final String name;
    private final String timestamp;
    private final String url;
    private final String ip;

    @Builder
    public VisitorDto(String created, Integer hashId, String name, String timestamp, String url, String ip) {
        super(created, hashId);
        this.name = name;
        this.timestamp = timestamp;
        this.url = url;
        this.ip = ip;
    }
}
