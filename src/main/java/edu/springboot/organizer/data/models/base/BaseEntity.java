package edu.springboot.organizer.data.models.base;

import edu.springboot.organizer.data.decriptor.Cryptor;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public abstract class BaseEntity {
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

    public void setId(String timeStampId) {
        if (this.id == null) {
            this.id = timeStampId;
            this.hashId = Cryptor.encryptWord(this.id);
        }
    }

    @Getter
    public enum BaseConst {
        ID("id");
        private final String column;

        BaseConst(String column) {
            this.column = column;
        }
    }

    @Override
    public String toString() {
        return id + '\'' +
                ", hashId=" + hashId;
    }
}
