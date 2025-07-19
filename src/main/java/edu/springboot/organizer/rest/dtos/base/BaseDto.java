package edu.springboot.organizer.rest.dtos.base;

import edu.springboot.organizer.data.decriptor.Cryptor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public abstract class BaseDto {
    private String created;
    private Integer hashId;

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        BaseDto that = (BaseDto) o;
        return created.equals(that.created);
    }

    @Override
    public int hashCode() {
        if (hashId == null) {
            this.hashId = Cryptor.encryptWord(this.created);
        }
        return this.hashId;
    }

    public BaseDto(String created, Integer hashId) {
        this.created = created;
        hashCode();
    }
}
