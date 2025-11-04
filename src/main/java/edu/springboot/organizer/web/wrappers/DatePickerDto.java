package edu.springboot.organizer.web.wrappers;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

@Data
public class DatePickerDto implements Serializable {
    @DateTimeFormat(pattern = "yyyy-MM")
    private Date lookDate; // html form handles only a Date type using this format

    private String dateFormat = "yyyy-MM";

}
