package edu.springboot.organizer.data.models;

import edu.springboot.organizer.data.models.base.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true, of = {})
public class Credential extends BaseEntity {
    public static final String TABLE_NAME = "credentials";
    private String login;
    private String password;
    private String role;
    private String email;

    @Getter
    public enum Const {
        LOGIN("login"),
        PASS("password"),
        ROLE("role"),
        EMAIL("email");
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
                        "UNIQUE(%s), " +
                        "UNIQUE(%s), " +
                        "UNIQUE(%s));",
                TABLE_NAME,
                BaseEntity.BaseConst.ID.getColumn(),
                Const.LOGIN.getColumn(),
                Const.PASS.getColumn(),
                Const.ROLE.getColumn(),
                Const.EMAIL.getColumn(),
                Const.LOGIN.getColumn(),
                Const.PASS.getColumn(),
                Const.EMAIL.getColumn()
        );
    }

}
