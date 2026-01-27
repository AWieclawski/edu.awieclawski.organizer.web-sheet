package edu.springboot.organizer.web.dtos.base;

import edu.springboot.organizer.data.decriptor.Cryptor;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.Arrays;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public abstract class BaseDto {

    private final static String LIMITER = " | ";

    protected String created;     // as ID
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

    public boolean hasError() {
        return this.errorMessage != null;
    }

    /**
     * Method to validate after.
     *
     */
    public abstract void validate();

    /**
     * Method to populate generated fields values
     *
     */
    public abstract void autoUpdate();

    public List<String> getErrorList() {
        String nextStep = errorMessage != null ? errorMessage.replace(LIMITER, "#") : "";
        return Arrays.asList(nextStep.split("#"));
    }

    protected void handleErrorMessage(String message) {
        this.errorMessage = this.errorMessage == null ? message : this.errorMessage + LIMITER + message;
    }
}
