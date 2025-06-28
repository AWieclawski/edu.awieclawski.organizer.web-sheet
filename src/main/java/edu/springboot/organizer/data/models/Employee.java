package edu.springboot.organizer.data.models;

import edu.springboot.organizer.data.models.base.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

/**
 * Dane pracownika dla, którego utworzony jest dany rekord miesięczny
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper = true, of = {})
public class Employee extends BaseEntity {
    public static final String TABLE_NAME = "employees";
    private String name;
    private String surName;
    private String uniqNick;

    @Getter
    public enum Const {
        NAME("name"),
        SURNAME("sur_name"),
        NICK("uniq_nick");
        private final String column;

        Const(String column) {
            this.column = column;
        }
    }
}
