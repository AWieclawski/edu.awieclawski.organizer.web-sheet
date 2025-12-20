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
 * A monthly record created for a given month (year) and user,
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
public class MonthRecord extends BaseEntity {
    public static final String TABLE_NAME = "month_records";
    private String employee;
    private String setId;
    private Integer standardHours;

    @Getter
    public enum Const {
        EMPLOYEE("employee"),
        SET("set_id"),
        STD_HRS("std_hrs");

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
                        "%s INTEGER);",
                TABLE_NAME,
                BaseConst.ID.getColumn(),
                Const.SET.getColumn(),
                Const.EMPLOYEE.getColumn(),
                Const.STD_HRS.getColumn()
        );
    }
}
