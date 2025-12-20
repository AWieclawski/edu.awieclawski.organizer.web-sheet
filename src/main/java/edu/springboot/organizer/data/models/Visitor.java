package edu.springboot.organizer.data.models;

import edu.springboot.organizer.data.models.base.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true, of = {})
public class Visitor extends BaseEntity {
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

    public static String getSqlTableCreator() {
        return String.format("CREATE TABLE IF NOT EXISTS %s ( " +
                        "%s TEXT PRIMARY KEY, " +
                        "%s TEXT, " +
                        "%s DATETIME DEFAULT CURRENT_TIMESTAMP, " +
                        "%s TEXT, " +
                        "%s TEXT); ",
                TABLE_NAME,
                BaseConst.ID.getColumn(),
                Const.NAME.getColumn(),
                Const.TIMESTAMP.getColumn(),
                Const.URL.getColumn(),
                Const.IP.getColumn());
    }

}

