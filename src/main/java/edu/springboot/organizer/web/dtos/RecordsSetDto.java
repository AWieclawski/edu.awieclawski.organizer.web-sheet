package edu.springboot.organizer.web.dtos;

import edu.springboot.organizer.generator.contracts.DateMonthHolder;
import edu.springboot.organizer.web.dtos.base.BaseDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.Month;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Setter
@Getter
@EqualsAndHashCode(callSuper = true, of = {})
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class RecordsSetDto extends BaseDto implements DateMonthHolder {
    private Integer year;
    private Integer month;
    private String userId;
    private List<MonthRecordDto> monthRecords;

    @Override
    public String getLocalDate(int day) {
        return DateMonthHolder.buildLocaLDate(day, this.month, this.year);
    }

    public String getDisplayMonthNo() {
        return this.month != null ? String.format("%02d", this.month) : "";
    }

    public void addMonthRecords(List<MonthRecordDto> monthRecordDtos) {
        if (this.monthRecords == null) {
            this.monthRecords = new ArrayList<>();
        }
        this.monthRecords.addAll(monthRecordDtos);
    }

    public String getMonthNameByLocale(Locale locale) {
        return Month.of(month).getDisplayName(TextStyle.FULL_STANDALONE, locale);
    }

    private Integer handleMonth(Integer month) {
        if (month != null && (month > 12 || month < 1)) {
            handleErrorMessage((month + " <- Month value not valid!"));
        }
        return month;
    }

    @Override
    public BaseDto validate() {
        this.month = handleMonth(month);
        return this;
    }
}
