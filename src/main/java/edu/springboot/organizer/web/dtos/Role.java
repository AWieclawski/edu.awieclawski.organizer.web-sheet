package edu.springboot.organizer.web.dtos;

public enum Role {
    ROLE_USER,
    ROLE_ADMIN;

    public static Role findByName(String name) {
        for (Role v : values()) {
            if (v.name().equals(name)) {
                return v;
            }
        }
        return null;
    }

}
