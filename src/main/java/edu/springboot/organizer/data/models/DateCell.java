package edu.springboot.organizer.data.models;

import edu.springboot.organizer.data.models.base.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

/**
 * A cell containing data for the day of the month,
 * from which rows of cells of a given type are built
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true, of = {})
public class DateCell extends BaseEntity {
    public static final String TABLE_NAME = "date_cells";
    private LocalDate localDate;
    private String workType;
    private Integer hours;
    private Integer beginHour;
    private Integer endHour;
    private Integer overtime;
    private Integer holiday;
    private String weekDay;
    private String monthRecordId;

    @Getter
    public enum Const {
        DATE("local_date"),
        WORK_TYPE("work_type"),
        HOURS("hours"),
        BEGIN_HOUR("begin_hour"),
        END_HOUR("end_hour"),
        OVERTIME("overtime"),
        HOLIDAY("holiday"),
        WEEK_DAY("week_day"),
        MONTH_RECORD("month_record_id");
        private final String column;

        Const(String column) {
            this.column = column;
        }
    }

    public static String getSqlTableCreator() {
        return String.format("CREATE TABLE IF NOT EXISTS %s " +
                        "(%s TEXT PRIMARY KEY, " +
                        "%s DATETIME, " +
                        "%s TEXT, " +
                        "%s INTEGER, " +
                        "%s INTEGER, " +
                        "%s INTEGER, " +
                        "%s INTEGER, " +
                        "%s INTEGER DEFAULT 0 NOT NULL, " +
                        "%s TEXT, " +
                        "%s TEXT);",
                TABLE_NAME,
                BaseConst.ID.getColumn(),
                Const.DATE.getColumn(),
                Const.WORK_TYPE.getColumn(),
                Const.BEGIN_HOUR.getColumn(),
                Const.END_HOUR.getColumn(),
                Const.HOURS.getColumn(),
                Const.OVERTIME.getColumn(),
                Const.HOLIDAY.getColumn(),
                Const.WEEK_DAY.getColumn(),
                Const.MONTH_RECORD.getColumn());
    }
}
