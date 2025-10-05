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
 * A seto of monthly records created for a given month (year) and user,
 * containing rows with lists of elements such as: hour range,
 * number of hours, overtime <- for each day of that month
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper = true, of = {})
@ToString(callSuper = true)
public class RecordsSet extends BaseEntity {
    public static final String TABLE_NAME = "records_set";
    private Integer year;
    private Integer month;
    private String userId;

    @Getter
    public enum Const {
        MONTH("month"),
        YEAR("year"),
        USER("user_id");


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
                        "%s TEXT);",
                TABLE_NAME,
                BaseConst.ID.getColumn(),
                Const.MONTH.getColumn(),
                Const.YEAR.getColumn(),
                Const.USER.getColumn()
        );
    }
}
