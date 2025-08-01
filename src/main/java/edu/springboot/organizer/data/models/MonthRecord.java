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
 * Rekord miesięczny utworzony dla danego miesiąca (roku) i użytkownika,
 * który zawiera wiersze z listami elementów typu:
 * zakres godzin, ilość godzin, nadgodziny <- na każdy dzień tego miesiąca
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper = true, of = {})
@ToString(callSuper = true)
public class MonthRecord extends BaseEntity {
    public static final String TABLE_NAME = "month_records";
    private Integer year;
    private Integer month;
    private String userId;
    private String employee;

    @Getter
    public enum Const {
        MONTH("month"),
        YEAR("year"),
        USER("user_id"),
        EMPLOYEE("employee");

        private final String column;
        Const(String column) {
            this.column = column;
        }

    }

    public static String getSqlTableCreator() {
        return String.format("CREATE TABLE IF NOT EXISTS %s ( " +
                        "%s TEXT PRIMARY KEY, " +
                        "%s INTEGER, " +
                        "%s INTEGER, " +
                        "%s TEXT, " +
                        "%s TEXT);",
                TABLE_NAME,
                BaseConst.ID.getColumn(),
                Const.MONTH.getColumn(),
                Const.YEAR.getColumn(),
                Const.USER.getColumn(),
                Const.EMPLOYEE.getColumn()
        );
    }
}
