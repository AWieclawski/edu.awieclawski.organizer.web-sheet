package edu.awieclawski.organizer.data.models.base;

import edu.awieclawski.organizer.data.decriptor.Cryptor;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class BaseEntity {
    private String id;
    private Integer hashId;

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;

        BaseEntity that = (BaseEntity) o;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        if (hashId == null) {
            this.hashId = Cryptor.encryptWord(this.id);
        }
        return this.hashId;
    }

    @Getter
    public enum BaseConst {
        ID("id");
        private final String column;

        BaseConst(String column) {
            this.column = column;
        }
    }
}
