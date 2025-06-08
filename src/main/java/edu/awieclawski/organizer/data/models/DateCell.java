package edu.awieclawski.organizer.data.models;

import edu.awieclawski.organizer.data.models.base.BaseEntity;
import edu.awieclawski.organizer.generator.enums.CellType;
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
}
