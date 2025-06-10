package edu.springboot.organizer.generator.dtos.base;

import edu.springboot.organizer.data.decriptor.Cryptor;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class BaseDto {
    private String id;
    private Integer hashId;

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;

        BaseDto that = (BaseDto) o;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        if (hashId == null) {
            this.hashId = Cryptor.encryptWord(this.id);
        }
        return this.hashId;
    }
}
