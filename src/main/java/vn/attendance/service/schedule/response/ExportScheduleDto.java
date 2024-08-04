package vn.attendance.service.schedule.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ExportScheduleDto {
    Integer id;
    String day;
    String roomName;
    String className;
    String subjectCode;
    String teacherCode;
    String slotName;
}
