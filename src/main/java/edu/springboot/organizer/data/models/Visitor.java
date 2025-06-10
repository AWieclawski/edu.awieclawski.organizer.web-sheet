package edu.springboot.organizer.data.models;

import edu.springboot.organizer.data.models.base.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

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
public class Visitor extends BaseEntity  {
    public static final String TABLE_NAME = "visitors";
    private String name;
    private LocalDateTime timestamp;
    private String url;
    private String ip;

    @Getter
    public enum Const {
        NAME("name"),
        URL("url"),
        IP("ip"),
        TIMESTAMP("timestamp");
        private final String column;
        Const(String column) {
            this.column = column;
        }
    }

}

