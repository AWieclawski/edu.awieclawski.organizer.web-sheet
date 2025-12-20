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
 * The entity of the user using the application (after logging in)
 * and having access to the DB records they created
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true, of = {})
public class User extends BaseEntity {
    public static final String TABLE_NAME = "users";
    private String name;
    private String surName;
    private String credentialId;

    @Getter
    public enum Const {
        NAME("name"),
        SURNAME("sur_name"),
        CREDENTIAL("credential_id");
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
                        "%s TEXT NOT NULL, " +
                        "UNIQUE(%s));",
                TABLE_NAME,
                BaseConst.ID.getColumn(),
                Const.NAME.getColumn(),
                Const.SURNAME.getColumn(),
                Const.CREDENTIAL.getColumn(),
                Const.CREDENTIAL.getColumn());
    }

}
