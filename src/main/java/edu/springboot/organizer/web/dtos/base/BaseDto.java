package edu.springboot.organizer.web.dtos.base;

import edu.springboot.organizer.data.decriptor.Cryptor;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    protected Map<String, String> errorMap = new HashMap<>();

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
        return getErrorMap() != null ? new ArrayList<>(getErrorMap().values()) : new ArrayList<>();
    }

    public String getFromErrorMap(String key) {
        if (key != null && getErrorMap() != null) {
            return getErrorMap().get(key);
        }
        return null;
    }

    public void addToErrorMap(String key, String value) {
        if (key != null && getErrorMap() != null) {
            getErrorMap().put(key, value);
            handleErrorMessage(value);
        }
    }

    protected void handleErrorMessage(String message) {
        this.errorMessage = this.errorMessage == null ? message : this.errorMessage + LIMITER + message;
    }

    public Map<String, String> getErrorMap() {
        if (this.errorMap == null) {
            this.errorMap = new HashMap<>();
        }
        return this.errorMap;
    }

}
