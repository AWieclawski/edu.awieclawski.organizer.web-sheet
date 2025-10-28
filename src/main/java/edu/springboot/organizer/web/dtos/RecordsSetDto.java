package edu.springboot.organizer.web.dtos;

import edu.springboot.organizer.generator.contracts.DateMonthHolder;
import edu.springboot.organizer.web.dtos.base.BaseDto;
import edu.springboot.organizer.web.exceptions.DtoValidationException;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.time.Month;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Getter
@EqualsAndHashCode(callSuper = true, of = {})
public class RecordsSetDto extends BaseDto implements DateMonthHolder {
    private final Integer year;
    private final Integer month;
    private final String userId;
    private List<MonthRecordDto> monthRecords;

    @Builder
    public RecordsSetDto(String created,
                         Integer hashId,
                         Integer year,
                         Integer month,
                         String userId,
                         List<MonthRecordDto> monthRecords) {
        super(created, hashId);
        this.year = year;
        this.month = handleMonth(month);
        this.userId = userId;
        this.monthRecords = monthRecords;
    }

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
            throw new DtoValidationException((month + " <- Month value not valid!"));
        }
        return month;
    }
}
