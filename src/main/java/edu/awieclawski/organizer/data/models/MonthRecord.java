package edu.awieclawski.organizer.data.models;

import edu.awieclawski.organizer.data.models.base.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
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
@EqualsAndHashCode(callSuper = true)
public class MonthRecord extends BaseEntity {
    public static final String TABLE_NAME = "month_records";
    private Integer year;
    private Integer month;
    private String visitorId;

    @Getter
    public enum Const {
        ID("id"),
        MONTH("month"),
        YEAR("year"),
        VISITOR("visitor_id");
        private final String column;

        Const(String column) {
            this.column = column;
        }
    }
}
