package edu.springboot.organizer.data.models;

import edu.springboot.organizer.data.models.base.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

/**
 * Details of the employee for whom a given monthly record is created
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true, of = {})
public class Employee extends BaseEntity {
    public static final String TABLE_NAME = "employees";
    private String name;
    private String surName;
    private String uniqNick;
    private String userId;

    @Getter
    public enum Const {
        NAME("name"),
        SURNAME("sur_name"),
        NICK("uniq_nick"),
        USER("user_id");

        private final String column;

        Const(String column) {
            this.column = column;
        }

    }

    public static String getSqlTableCreator() {
        return String.format("CREATE TABLE IF NOT EXISTS %s ( " +
                        "%s TEXT PRIMARY KEY, " +
                        "%s TEXT, " +
                        "%s TEXT, " +
                        "%s TEXT, " +
                        "%s TEXT, " +
                        "UNIQUE(%s,%s,%s));",
                TABLE_NAME,
                BaseConst.ID.getColumn(),
                Const.NAME.getColumn(),
                Const.SURNAME.getColumn(),
                Const.NICK.getColumn(),
                Const.USER.getColumn(),
                Const.NAME.getColumn(),
                Const.SURNAME.getColumn(),
                Const.USER.getColumn()
        );
    }
}
