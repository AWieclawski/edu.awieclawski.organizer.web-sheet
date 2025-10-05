package edu.springboot.organizer.web.dtos;

import edu.springboot.organizer.generator.contracts.DateMonthHolder;
import edu.springboot.organizer.web.dtos.base.BaseDto;
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
    private final List<MonthRecordDto> monthRecordDtoList;

    @Builder
    public RecordsSetDto(String created,
                         Integer hashId,
                         Integer year,
                         Integer month,
                         String userId) {
        super(created, hashId);
        this.year = year;
        this.month = handleMonth(month);
        this.userId = userId;
        this.monthRecordDtoList = new ArrayList<>();
    }

    @Override
    public String getLocalDate(int day) {
        return DateMonthHolder.buildLocaLDate(day, this.month, this.year);
    }

    public void addMonthRecords(List<MonthRecordDto> monthRecordDtos) {
        this.monthRecordDtoList.addAll(monthRecordDtos);
    }

    public String getMonthNameByLocale(Locale locale) {
        return Month.of(month).getDisplayName(TextStyle.FULL_STANDALONE, locale);
    }

    private Integer handleMonth(Integer month) {
        if (month != null && (month > 12 || month < 1)) {
            throw new IllegalArgumentException((month + " <- Month value not valid!"));
        }
        return month;
    }
}
