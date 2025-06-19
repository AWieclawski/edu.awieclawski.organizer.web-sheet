package edu.springboot.organizer.data.models;

import edu.springboot.organizer.data.models.base.BaseEntity;
import edu.springboot.organizer.generator.enums.CellType;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

/**
 * Komórka opisująca dany dzień, która jest elementem listy komórek danego typu
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper = true, of = {})
public class DateCell extends BaseEntity {
    public static final String TABLE_NAME = "date_cells";
    private LocalDate localDate;
    private Integer hours;
    private CellType cellType;
    private Integer beginHour;
    private Integer endHour;
    private String monthRecordId;

    @Getter
    public enum Const {
        DATE("local_date"),
        HOURS("hours"),
        CELL_TYPE("cell_type"),
        BEGIN_HOUR("begin_hour"),
        END_HOUR("end_hour"),
        MONTH_RECORD("month_record_id");
        private final String column;

        Const(String column) {
            this.column = column;
        }
    }

    public static String getSqlTableCreator() {
        return String.format("CREATE TABLE IF NOT EXISTS %s (%s TEXT PRIMARY KEY, %s DATETIME, %s INTEGER, %s INTEGER, %s INTEGER, %s TEXT);",
                TABLE_NAME,
                BaseConst.ID.getColumn(),
                Const.DATE.getColumn(),
                Const.BEGIN_HOUR.getColumn(),
                Const.END_HOUR.getColumn(),
                Const.HOURS.getColumn(),
                Const.MONTH_RECORD.getColumn());
    }
}
