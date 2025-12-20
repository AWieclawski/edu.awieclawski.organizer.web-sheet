package edu.springboot.organizer.web.enums;

import lombok.Getter;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public enum WorkType {
    T("Working time"),
    W("Vacation leave"),
    O("Child care"),
    L4("Sick leave");

    @Getter
    private final String description;

    WorkType(String description) {
        this.description = description;
    }

    public static List<String> getDescriptions() {
        return Arrays.stream(WorkType.values()).map(WorkType::getDescription).collect(Collectors.toList());
    }

    public WorkType getByDescription(String search) {
        return Arrays.stream(WorkType.values()).filter(it -> it.description.equals(search)).findFirst().orElse(T);
    }

    public static WorkType getWorkTypeFromString(String search) {
        try {
            return Enum.valueOf(WorkType.class, search);
        } catch (NullPointerException e) {
            // ignore
        }
        return T;
    }
}
