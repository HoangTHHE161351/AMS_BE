package vn.attendance.util;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class Week {
    LocalDate firstDate;
    LocalDate lastDate;
}
