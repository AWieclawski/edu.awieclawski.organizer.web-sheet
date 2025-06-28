package edu.springboot.organizer.data.models;

import edu.springboot.organizer.data.models.base.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

/**
 * Encja użytkownika korzystającego z aplikacji (po zalogowaniu)
 * i posiadającego dostęp do zapisów w BD, które utworzył
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper = true, of = {})
public class User extends BaseEntity {
    public static final String TABLE_NAME = "users";
    private String name;
    private String surName;
    private String email;
    private String login;
    private String password;

    @Getter
    public enum Const {
        NAME("name"),
        SUR_NAME("sur_name"),
        EMAIL("email"),
        LOGIN("login"),
        PASS("pass");
        private final String column;

        Const(String column) {
            this.column = column;
        }
    }

    public static String getSqlTableCreator() {
        return String.format("CREATE TABLE IF NOT EXISTS %s ( %s TEXT PRIMARY KEY, %s TEXT, %s TEXT, %s TEXT, %s TEXT, %s TEXT, UNIQUE(%s));",
                TABLE_NAME,
                BaseConst.ID.getColumn(),
                Const.NAME.getColumn(),
                Const.SUR_NAME.getColumn(),
                Const.EMAIL.getColumn(),
                Const.LOGIN.getColumn(),
                Const.PASS.getColumn(),
                Const.LOGIN.getColumn());
    }

}
