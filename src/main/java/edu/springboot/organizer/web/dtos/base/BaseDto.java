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

    public boolean hasError() {
        return this.errorMessage != null;
    }

    /**
     * Method to validate after.
     *
     * @return
     */
    public abstract void validate();

    /**
     * Method to populate generated fields values
     *
     * @return
     */
    public abstract void autoUpdate();

    protected void handleErrorMessage(String message) {
        this.errorMessage = this.errorMessage == null ? message : this.errorMessage + " | " + message;
    }
}
