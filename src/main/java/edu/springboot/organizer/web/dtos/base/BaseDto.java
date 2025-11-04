package edu.springboot.organizer.web.dtos.base;

import edu.springboot.organizer.data.decriptor.Cryptor;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public abstract class BaseDto {
    protected String created;
    protected Integer hashId;
    protected String errorMessage;

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

    protected void handleErrorMessage(String message) {
        this.errorMessage = errorMessage == null ? message : errorMessage + " | " + message;
    }

    /**
     * Method to populate generated fields values and validate after that.
     *
     * @return
     */
    public abstract BaseDto validate();
}
