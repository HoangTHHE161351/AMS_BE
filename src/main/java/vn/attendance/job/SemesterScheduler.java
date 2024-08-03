package vn.attendance.job;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import vn.attendance.model.Semester;
import vn.attendance.repository.SemesterRepository;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.time.temporal.TemporalField;
import java.time.temporal.WeekFields;
import java.util.Locale;

@Component
public class SemesterScheduler {

    @Autowired
    SemesterRepository semesterRepository;

    @Scheduled(cron = "0 0 0 ? 5 MON#3")
    @Scheduled(cron = "0 0 0 ? 10 MON#3")
    public void AutoAddSemester() {
        // Lấy ngày hiện tại
        LocalDate now = LocalDate.now();
        TemporalField weekOfYear = WeekFields.of(Locale.getDefault()).weekOfYear();
        int currentWeek = now.get(weekOfYear);

        if (currentWeek == 21) {
            // Tạo các học kỳ bắt đầu từ tuần 31 của năm hiện tại
            createAndSaveSemesterFromWeek(now.getYear(), 31);
        } else if (currentWeek == 41) {
            // Tạo các học kỳ bắt đầu từ tuần đầu tiên của năm tiếp theo
            createAndSaveSemesterFromWeek(now.plusYears(1).getYear(), 1);
        }
    }

    private void createAndSaveSemesterFromWeek(int year, int startWeek) {
        // Tính toán ngày bắt đầu và kết thúc của các giai đoạn
        LocalDate startSemester1 = getFirstDayOfWeek(year, startWeek, DayOfWeek.MONDAY);
        LocalDate endSemester1 = startSemester1.plusWeeks(8).with(TemporalAdjusters.previousOrSame(DayOfWeek.SATURDAY));

        LocalDate startSemester2 = endSemester1.plusWeeks(3).plusDays(1); // Bắt đầu sau 3 tuần từ kết thúc của kỳ 1
        LocalDate endSemester2 = startSemester2.plusWeeks(8).with(TemporalAdjusters.previousOrSame(DayOfWeek.SATURDAY));

        LocalDate startSemester3 = endSemester2.plusWeeks(1).plusDays(1); // Bắt đầu ngay sau kết thúc của kỳ 2
        LocalDate endSemester3 = startSemester3.plusWeeks(1).with(TemporalAdjusters.previousOrSame(DayOfWeek.SATURDAY));

        // Tạo các đối tượng Semester và lưu vào cơ sở dữ liệu
        saveSemester("S" + startWeek + "p1y" + year, startSemester1, endSemester1);
        saveSemester("S" + startWeek + "p2y" + year, startSemester2, endSemester2);
        saveSemester("S" + startWeek + "p3y" + year, startSemester3, endSemester3);
    }

    private LocalDate getFirstDayOfWeek(int year, int week, DayOfWeek dayOfWeek) {
        return LocalDate.ofYearDay(year, 1)
                .with(WeekFields.of(Locale.getDefault()).weekOfYear(), week)
                .with(TemporalAdjusters.previousOrSame(dayOfWeek));
    }

    private void saveSemester(String semesterName, LocalDate startTime, LocalDate endTime) {
        Semester semester = new Semester();
        semester.setSemesterName(semesterName);
        semester.setStartTime(startTime.atStartOfDay());
        semester.setEndTime(endTime.atStartOfDay());
        semesterRepository.save(semester);
    }
}
