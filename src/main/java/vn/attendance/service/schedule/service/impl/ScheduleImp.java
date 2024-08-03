package vn.attendance.service.schedule.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.attendance.config.authen.BaseUserDetailsService;
import vn.attendance.exception.AmsException;
import vn.attendance.model.*;
import vn.attendance.repository.*;
import vn.attendance.service.attendance.request.AttendanceDto;
import vn.attendance.service.attendance.request.IAttendanceDto;
import vn.attendance.service.schedule.request.AddScheduleRequest;
import vn.attendance.service.schedule.request.EditScheduleRequest;
import vn.attendance.service.schedule.response.IScheduleDto;
import vn.attendance.service.schedule.response.ScheduleDto;
import vn.attendance.service.schedule.service.ScheduleService;
import vn.attendance.util.Constants;
import vn.attendance.util.MessageCode;

import javax.annotation.PostConstruct;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
public class ScheduleImp implements ScheduleService {

    @Autowired
    ScheduleRepository scheduleRepository;

    @Autowired
    UsersRepository userRepository;

    @Autowired
    ClassRoomRepository classRoomRepository;

    @Autowired
    SubjectRepository subjectRepository;

    @Autowired
    TimeSlotRepository timeSlotRepository;

    @Autowired
    RoomRepository roomRepository;

    @Autowired
    SemesterRepository semesterRepository;

    @Autowired
    TeacherRepository teacherRepository;

    @Autowired
    StudentRepository studentRepository;

    @Autowired
    StudentClassRepository studentClassRepository;

    @Autowired
    ClassSubjectRepository classSubjectRepository;

    @Autowired
    TeacherSubjectRepository teacherSubjectRepository;

    @Autowired
    AttendanceRepository attendanceRepository;

    private Map<String, Integer> roomIndexMap;
    private Map<String, Integer> timeSlotIndexMap;
    private Map<String, Integer> teacherIndexMap;
    private Map<String, Integer> subIndexMap;
    private Map<String, Integer> classIndexMap;
    private Map<String, Integer> semesterIndexMap;

    private List<Schedule> scheduleList;
    List<Schedule> schedules;

    @PostConstruct
    private void init() {
        // Tạo các map
        roomIndexMap = roomRepository.findAllRooms().stream()
                .filter(room -> room.getRoomName() != null)
                .collect(Collectors.toMap(room -> room.getRoomName(), Room::getId));

        timeSlotIndexMap = timeSlotRepository.findAllTimeSlots().stream()
                .filter(timeSlot -> timeSlot.getSlotName() != null)
                .collect(Collectors.toMap(timeSlot -> timeSlot.getSlotName(), TimeSlot::getId));

        classIndexMap = classRoomRepository.findAllClassRoom().stream()
                .filter(cl -> cl.getClassName() != null)
                .collect(Collectors.toMap(classRoom -> classRoom.getClassName(), ClassRoom::getId));

        subIndexMap = subjectRepository.findAllSubjects().stream()
                .filter(s -> s.getCode() != null)
                .collect(Collectors.toMap(s -> s.getCode(), Subject::getId));

        teacherIndexMap = teacherRepository.findAllRoleTeacher().stream()
                .filter(users -> users.getUsername() != null)
                .collect(Collectors.toMap(users -> users.getUsername(), Users::getId));

        semesterIndexMap = semesterRepository.findAllSemesters().stream()
                .filter(semester -> semester.getSemesterName() != null)
                .collect(Collectors.toMap(semester -> semester.getSemesterName(), Semester::getId));
    }

    @Override
    public IScheduleDto getScheduleById(Integer id) throws AmsException {
        Users users = BaseUserDetailsService.USER.get();
        if (users == null) {
            throw new AmsException(MessageCode.USER_NOT_FOUND);
        }
        if (users.getRoleId() != 1 && users.getRoleId() != 2) {
            throw new AmsException(MessageCode.USER_NOT_AUTHORITY);
        }
        return scheduleRepository.findScheduleById(id);
    }

    @Override
    public String[][] findScheduleByUserId(LocalDate date) throws Exception {

        Users users = BaseUserDetailsService.USER.get();
        if (users == null) {
            throw new AmsException(MessageCode.USER_NOT_FOUND);
        }
        if (users.getRoleId() != 1 && users.getRoleId() != 2 && users.getRoleId() != 3) {
            throw new AmsException(MessageCode.USER_NOT_AUTHORITY);
        }

        int col = timeSlotRepository.countTimeSlot();
        int row = roomRepository.countRoom();

        String[][] schedule = new String[row + 1][col + 1];
        List<TimeSlot> timeSlotList = timeSlotRepository.findAllTimeSlots();
        List<Room> roomList = roomRepository.findAllRooms();
        Semester semester = semesterRepository.findSemesters(date);
        List<ScheduleDto> scheduleList = new ArrayList<>();

        if (users.getRoleId() == 1 || users.getRoleId() == 2) {
            scheduleList = scheduleRepository.findAllScheduleDto(date, semester.getId(), null);
        } else if (users.getRoleId() == 3) {
            scheduleList = scheduleRepository.findAllScheduleDto(date, semester.getId(), users.getId());
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");

        Map<String, Integer> rowIndex = new HashMap<>();
        for (int i = 1; i <= row; i++) {
            schedule[i][0] = roomList.get(i - 1).getRoomName();
            rowIndex.put(roomList.get(i - 1).getRoomName(), i);
        }

        Map<String, Integer> colIndex = new HashMap<>();
        for (int j = 1; j <= col; j++) {
            schedule[0][j] = timeSlotList.get(j - 1).getSlotName() +
                    "\n(" + timeSlotList.get(j - 1).getStartTime().format(formatter) +
                    " - " + timeSlotList.get(j - 1).getEndTime().format(formatter) + ")";
            colIndex.put(timeSlotList.get(j - 1).getSlotName(), j);
        }

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        // Tạo các map
        Map<String, Integer> roomIndexMap = new HashMap<>();
        for (Room room : roomList) {
            roomIndexMap.put(room.getRoomName(), room.getId());
        }

        Map<String, Integer> timeSlotIndexMap = new HashMap<>();
        for (TimeSlot timeSlot : timeSlotList) {
            timeSlotIndexMap.put(timeSlot.getSlotName(), timeSlot.getId());
        }

        // Get all
        boolean isAdmin = (users.getRoleId() == 1 || users.getRoleId() == 2);

        for (Map.Entry<String, Integer> roomEntry : rowIndex.entrySet()) {
            for (Map.Entry<String, Integer> timeSlotEntry : colIndex.entrySet()) {
                String roomName = roomEntry.getKey();
                int roomIndex = roomEntry.getValue();
                String timeSlotDesc = timeSlotEntry.getKey();
                int timeSlotIndex = timeSlotEntry.getValue();

                boolean isScheduled = false;
                for (ScheduleDto dto : scheduleList) {
                    if ((isAdmin && dto.getRoom().equals(roomName) && dto.getTime().equals(timeSlotDesc)) ||
                            (dto.getUsersId() == users.getId() && dto.getRoom().equals(roomName) &&
                                    dto.getTime().equals(timeSlotDesc))) {

                        schedule[roomIndex][timeSlotIndex] = objectMapper.writeValueAsString(dto);
                        isScheduled = true;
                        break;
                    }
                }
                if (!isScheduled) {
                    ScheduleDto scheduleDto = new ScheduleDto();
                    scheduleDto.setSemester(semester.getSemesterName());
                    scheduleDto.setRoom(roomName);
                    scheduleDto.setTime(timeSlotDesc);
                    scheduleDto.setDate(date.atStartOfDay());
                    schedule[roomIndex][timeSlotIndex] = objectMapper.writeValueAsString(scheduleDto);
                }
            }
        }

        return schedule;
    }

    @Override
    public String[][] findScheduleByStudentId(LocalDate from, LocalDate to) throws Exception {

        Users users = BaseUserDetailsService.USER.get();
        if (users == null) {
            throw new AmsException(MessageCode.USER_NOT_FOUND);
        }
        if (users.getRoleId() != 4) {
            throw new AmsException(MessageCode.USER_NOT_AUTHORITY);
        }

        int row = timeSlotRepository.countTimeSlot();
        int col = 7;

        String[][] schedule = new String[row + 1][col + 1];
        List<TimeSlot> timeSlotList = timeSlotRepository.findAllTimeSlots();
        Semester semester = semesterRepository.findSemesters(LocalDate.now());
        List<IAttendanceDto> scheduleList = attendanceRepository.findAttendanceInDate(semester.getSemesterName(),
                users.getId(), from, to).stream().toList();
        List<AttendanceDto> scheduleDtoList = scheduleList.stream().map(AttendanceDto::new).toList();


        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd/MM");

        List<LocalDate> weeks = new ArrayList<>();

        LocalDate current = from;
        while (current.isBefore(to) || current.isEqual(to)) {
            weeks.add(current);
            // Di chuyển đến tuần tiếp theo
            current = current.plusDays(1);
        }

        Map<String, Integer> colIndex = new HashMap<>();
        for (int i = 1; i <= col; i++) {
            schedule[0][i] = weeks.get(i - 1).getDayOfWeek().toString() +
                    "\n (" + weeks.get(i - 1).format(dateFormat) + ")";
            colIndex.put(weeks.get(i - 1).getDayOfWeek().toString(), i);
        }

        Map<String, Integer> rowIndex = new HashMap<>();
        for (int j = 1; j <= row; j++) {
            schedule[j][0] = timeSlotList.get(j - 1).getSlotName() +
                    "\n(" + timeSlotList.get(j - 1).getStartTime().format(formatter) +
                    " - " + timeSlotList.get(j - 1).getEndTime().format(formatter) + ")";
            rowIndex.put(timeSlotList.get(j - 1).getSlotName(), j);
        }

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        for (Map.Entry<String, Integer> slotEntry : rowIndex.entrySet()) {
            for (Map.Entry<String, Integer> dayInWeekEntry : colIndex.entrySet()) {
                String slot = slotEntry.getKey();
                int slotIndex = slotEntry.getValue();
                String day = dayInWeekEntry.getKey();
                int dayIndex = dayInWeekEntry.getValue();

                boolean isScheduled = false;

                for (AttendanceDto dto : scheduleDtoList) {
                    if (slot.equals(dto.getSlot()) && day.equals(dto.getDay().toUpperCase())) {
                        schedule[slotIndex][dayIndex] = objectMapper.writeValueAsString(dto);
                        isScheduled = true;
                        break;
                    }
                }
                if (!isScheduled) {
                    schedule[slotIndex][dayIndex] = "-";
                }
            }
        }
        return schedule;
    }


    @Override
    @Transactional
    public List<AddScheduleRequest> importSchedule(List<AddScheduleRequest> stringList, String semesterName) throws AmsException {
        //Lấy thông tin User
        Users users = BaseUserDetailsService.USER.get();
        if (users == null) {
            throw new AmsException(MessageCode.USER_NOT_FOUND);
        }
        if (users.getRoleId() != 1 && users.getRoleId() != 2) {
            throw new AmsException(MessageCode.USER_NOT_AUTHORITY);
        }

        List<TimeSlot> timeSlotList = timeSlotRepository.findAllTimeSlots();
        Semester semester = semesterRepository.findSemestersByName(semesterName);
        scheduleList = scheduleRepository.finScheduleBySemester(semester.getId());
        Map<String, String> timeSlotIndexMap = timeSlotList.stream().collect(Collectors.toMap(TimeSlot::getSlotName, TimeSlot::getSlotName));

        if (semester == null)
            throw new AmsException(MessageCode.SEMESTER_NOT_FOUND);

        List<LocalDate> days;
        List<TimeSlot> slots = new ArrayList<>();

        for (AddScheduleRequest request : stringList) {
            char[] characters = request.getTimeSlot().toCharArray();
            String time = request.getTimeSlot();
            String timeOfDay = (characters[0] == 'P') ? "PM" : "AM";
            LocalDate currentData = request.getDate();
            days = getDatesFromDayOfWeek(request.getTimeSlot(), semester.getStartTime().toLocalDate(), semester.getEndTime().toLocalDate());

            for (TimeSlot slot : timeSlotList) {
                if (timeOfDay.equals("AM")) if (slot.getStartTime().isBefore(LocalTime.of(12, 0))) {
                    timeSlotIndexMap.put(slot.getSlotName(), slot.getSlotName());
                    slots.add(slot);
                }
                if (timeOfDay.equals("PM")) if (slot.getStartTime().isAfter(LocalTime.of(12, 0))) {
                    timeSlotIndexMap.put(slot.getSlotName(), slot.getSlotName());
                    slots.add(slot);
                }
            }

            for (int i = 1; i < characters.length; i++) {
                for (LocalDate localDate : days) {
                    schedules = scheduleRepository.findAllSchedules(localDate, semester.getId());
                    if (getDayOfWeekName(Character.getNumericValue(characters[i])) != null &&
                            timeSlotIndexMap.containsKey(slots.get(i - 1).getSlotName()) &&
                            localDate.getDayOfWeek().equals(getDayOfWeekName(Character.getNumericValue(characters[i])))) {

                        request.setTimeSlot(timeSlotIndexMap.get(slots.get(i - 1).getSlotName()));
                        request.setDate(localDate);
                        addSchedule(2, request, semesterName, localDate);
                        request.setTimeSlot(time);
                        request.setDate(currentData);
                    }
                }
            }

        }
        return stringList;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public AddScheduleRequest addSchedule(int option, AddScheduleRequest request, String semesterName, LocalDate date) throws AmsException {
        Users users = BaseUserDetailsService.USER.get();
        if (users == null) {
            throw new AmsException(MessageCode.USER_NOT_FOUND);
        }
        if (users.getRoleId() != 1 && users.getRoleId() != 2) {
            throw new AmsException(MessageCode.USER_NOT_AUTHORITY);
        }

        Integer semesterId = semesterIndexMap.get(semesterName);

        if (schedules == null) {
            scheduleList = scheduleRepository.finScheduleBySemester(semesterId);
            schedules = scheduleRepository.findAllSchedules(date, semesterId);
        }

        Map<Object, Integer> sheduleIndexMap = new HashMap<>();
        for (Schedule schedule : schedules) {
            sheduleIndexMap.put(schedule, schedule.getId());
        }

        Integer teacherId = teacherIndexMap.get(request.getTeacherCode().toLowerCase()),
                roomId = roomIndexMap.get(request.getRoom().toLowerCase()),
                classId = classIndexMap.get(request.getClassName().toLowerCase()),
                timeslotId = timeSlotIndexMap.get(request.getTimeSlot()),
                subId = subIndexMap.get(request.getSubjectCode().toLowerCase());

        ClassSubject classSubject = classSubjectRepository.findByClassAndSubId(classId, subId);
        TeacherSubject teacherSubject = teacherSubjectRepository.findByTeacherAndSubId(teacherId, subId);

        if (classSubject == null) {
            if (option == 1) {
                throw new AmsException(MessageCode.NOT_FOUND);
            }
            request.setStatus(Constants.REQUEST_STATUS.FAILED);
            request.setErrorMess("Data does not exist!");
            return request;
        }
        if (teacherSubject == null) {
            if (option == 1) {
                throw new AmsException(MessageCode.NOT_FOUND);
            }
            request.setStatus(Constants.REQUEST_STATUS.FAILED);
            request.setErrorMess("Data does not exist!");
            return request;
        }

        if (teacherId == null) {
            if (option == 1) {
                throw new AmsException(MessageCode.USER_NOT_FOUND);
            }
            request.setStatus(Constants.REQUEST_STATUS.FAILED);
            request.setErrorMess(MessageCode.USER_NOT_FOUND.getCode());
            return request;
        }
        if (roomId == null) {
            if (option == 1) {
                throw new AmsException(MessageCode.ROLE_NOT_FOUND);
            }
            request.setStatus(Constants.REQUEST_STATUS.FAILED);
            request.setErrorMess(MessageCode.ROOM_NOT_FOUND.getCode());
            return request;
        }
        if (classId == null) {
            if (option == 1) {
                throw new AmsException(MessageCode.CLASS_NOT_FOUND);
            }
            request.setStatus(Constants.REQUEST_STATUS.FAILED);
            request.setErrorMess(MessageCode.CLASS_NOT_FOUND.getCode());
            return request;
        }
        if (timeslotId == null) {
            if (option == 1) {
                throw new AmsException(MessageCode.TIMESLOT_NOT_FOUND);
            }
            request.setStatus(Constants.REQUEST_STATUS.FAILED);
            request.setErrorMess(MessageCode.TIMESLOT_NOT_FOUND.getCode());
            return request;
        }
        if (subId == null) {
            if (option == 1) {
                throw new AmsException(MessageCode.SCHEDULE_NOT_FOUND);
            }
            request.setStatus(Constants.REQUEST_STATUS.FAILED);
            request.setErrorMess(MessageCode.SUBJECT_NOT_FOUND.getCode());
            return request;
        }

        if (checkExit(sheduleIndexMap, request, timeslotId, roomId, classId, teacherId, subId, null) == null) {
            Schedule schedule = new Schedule();
            schedule.setSemester(semesterId);
            schedule.setUserId(teacherId);
            schedule.setRoomId(roomId);
            schedule.setClassId(classId);
            schedule.setSubjectId(subId);
            schedule.setTimeSlotId(timeslotId);
            schedule.setLearnTimestamp(request.getDate().atStartOfDay());
            schedule.setStatus(Constants.STATUS_TYPE.ACTIVE);
            schedule.setCreatedAt(LocalDateTime.now());
            schedule.setCreatedBy(users.getId());

            request.setStatus(Constants.REQUEST_STATUS.SUCCESS);
            schedule = scheduleRepository.save(schedule);
            scheduleList.add(schedule);
            Schedule finalSchedule = schedule;
            if (finalSchedule != null) {
                CompletableFuture.supplyAsync(() -> {
                    try {
                        List<StudentClass> studentList = studentClassRepository.findAllStudentClassByClassId(classId);
                        Attendance attendance = new Attendance();
                        attendance.setScheduleId(finalSchedule.getId());
                        attendance.setUserId(teacherId);
                        attendance.setStatus(Constants.STATUS_TYPE.NOT_YET);
                        attendance.setCreatedAt(LocalDateTime.now());
                        attendance.setCreatedBy(users.getId());
                        attendanceRepository.save(attendance);

                        for (StudentClass studentClass : studentList) {
                            attendance = new Attendance();
                            attendance.setScheduleId(finalSchedule.getId());
                            attendance.setUserId(studentClass.getStudentId());
                            attendance.setStatus(Constants.STATUS_TYPE.NOT_YET);
                            attendance.setCreatedAt(LocalDateTime.now());
                            attendance.setCreatedBy(users.getId());
                            attendanceRepository.save(attendance);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return null;
                });
            }
        }
        return request;
    }

    @Override
    public Object checkExit(Map<Object, Integer> scheduleIndexMap, Object request, Integer timeSlotId,
                            Integer roomId, Integer classId, Integer userId, Integer subId, Integer scheduleId) {

        for (Map.Entry<Object, Integer> entry : scheduleIndexMap.entrySet()) {
            Schedule schedule = (Schedule) entry.getKey();
            if (scheduleId == schedule.getId()) {
                continue;
            }
            if (schedule.getTimeSlotId().equals(timeSlotId) && schedule.getRoomId().equals(roomId) && schedule.getClassId().equals(classId)
                    && schedule.getUserId().equals(userId) && !schedule.getSubjectId().equals(subId)) {

                setRequestStatus(request, Constants.REQUEST_STATUS.FAILED, MessageCode.SCHEDULE_FAILED_SUBJECT.getCode());
                return request;
            }
            if (schedule.getTimeSlotId().equals(timeSlotId) && schedule.getRoomId().equals(roomId) && !schedule.getClassId().equals(classId)
                    && schedule.getUserId().equals(userId) && schedule.getSubjectId().equals(subId)) {

                setRequestStatus(request, Constants.REQUEST_STATUS.FAILED, MessageCode.SCHEDULE_FAILED_CLASS.getCode());
                return request;
            }
            if (schedule.getTimeSlotId().equals(timeSlotId) && schedule.getRoomId().equals(roomId) && schedule.getClassId().equals(classId)
                    && !schedule.getUserId().equals(userId) && schedule.getSubjectId().equals(subId)) {

                setRequestStatus(request, Constants.REQUEST_STATUS.FAILED, MessageCode.SCHEDULE_FAILED_TEACHER.getCode());
                return request;
            }
            if (schedule.getTimeSlotId().equals(timeSlotId) && schedule.getRoomId().equals(roomId) && schedule.getClassId().equals(classId)
                    && schedule.getUserId().equals(userId) && schedule.getSubjectId().equals(subId)) {

                setRequestStatus(request, Constants.REQUEST_STATUS.FAILED, MessageCode.SCHEDULE_ALREADY_EXISTS.getCode());
                return request;
            }
            if (schedule.getTimeSlotId().equals(timeSlotId) && schedule.getRoomId().equals(roomId) && !schedule.getClassId().equals(classId)) {
                setRequestStatus(request, Constants.REQUEST_STATUS.FAILED, MessageCode.SCHEDULE_FAILED_CLASS_TIME.getCode());
                return request;
            }
        }
        return null;
    }

    @Override
    public void setRequestStatus(Object request, String status, String errorMess) {
        if (request instanceof AddScheduleRequest addRequest) {
            addRequest.setStatus(status);
            addRequest.setErrorMess(errorMess);
        } else if (request instanceof EditScheduleRequest editRequest) {
            editRequest.setStatus(status);
            editRequest.setErrorMess(errorMess);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Schedule deleteSchedule(Integer id) throws AmsException {
        Users users = BaseUserDetailsService.USER.get();
        if (users == null) {
            throw new AmsException(MessageCode.USER_NOT_FOUND);
        }
//        if (users.getRoleId() != 1 && users.getRoleId() != 2) {
//            throw new AmsException(MessageCode.USER_NOT_AUTHORITY);
//        }
        Schedule schedule = scheduleRepository.findById(id).orElse(null);
        if (schedule == null || schedule.getStatus().equals(Constants.STATUS_TYPE.DELETED))
            throw new AmsException(MessageCode.SCHEDULE_NOT_FOUND);
        List<Attendance> attendances = attendanceRepository.findAttendanceByScheduleId(id);
        for (Attendance attendance : attendances
        ){
            attendance.setStatus(Constants.STATUS_TYPE.DELETED);
            attendance.setModifiedAt(LocalDateTime.now());
            attendance.setModifiedBy(users.getId());
            attendanceRepository.save(attendance);
        }
        schedule.setStatus(Constants.STATUS_TYPE.DELETED);
        schedule.setModifiedAt(LocalDateTime.now());
        schedule.setModifiedBy(users.getId());

        return scheduleRepository.save(schedule);
    }

    public List<LocalDate> getDatesFromDayOfWeek(String input, LocalDate fromDate, LocalDate toDate) {
        char[] characters = input.toCharArray();
        int[] daysOfWeek = new int[characters.length - 1];
        for (int i = 1; i < characters.length; i++) {
            daysOfWeek[i - 1] = Character.getNumericValue(characters[i]);
        }

        LocalDate startDate = fromDate;
        List<LocalDate> dates = new ArrayList<>();

        while (!startDate.isAfter(toDate)) {
            for (int dayOfWeek : daysOfWeek) {
                DayOfWeek targetDay = getDayOfWeekName(dayOfWeek);
                if (startDate.getDayOfWeek() == targetDay && !startDate.isAfter(toDate)) {
                    dates.add(startDate);
                }
            }
            startDate = startDate.plusDays(1);
        }
        return dates;
    }

    public DayOfWeek getDayOfWeekName(int dayOfWeek) {
        return switch (dayOfWeek) {
            case 2 -> DayOfWeek.MONDAY;
            case 3 -> DayOfWeek.TUESDAY;
            case 4 -> DayOfWeek.WEDNESDAY;
            case 5 -> DayOfWeek.THURSDAY;
            case 6 -> DayOfWeek.FRIDAY;
            case 7 -> DayOfWeek.SATURDAY;
            case 8 -> DayOfWeek.SUNDAY;
            default -> null;
        };
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public EditScheduleRequest editSchedule(EditScheduleRequest request, LocalDate date) throws AmsException {
        Users users = BaseUserDetailsService.USER.get();
        if (users == null) {
            throw new AmsException(MessageCode.USER_NOT_FOUND);
        }
        if (users.getRoleId() != 1 && users.getRoleId() != 2) {
            throw new AmsException(MessageCode.USER_NOT_AUTHORITY);
        }

        Semester semester = semesterRepository.findSemesters(date);
        scheduleList = scheduleRepository.findAllSchedules(date, semester.getId());

        Map<Object, Integer> sheduleIndexMap = new HashMap<>();
        for (Schedule schedule : scheduleList) {
            sheduleIndexMap.put(schedule, schedule.getId());
        }

        Integer teacherId = teacherIndexMap.get(request.getTeacherCode().toLowerCase()),
                roomId = roomIndexMap.get(request.getRoom().toLowerCase()),
                classId = classIndexMap.get(request.getClassName().toLowerCase()),
                timeslotId = timeSlotIndexMap.get(request.getTimeSlot()),
                subId = subIndexMap.get(request.getSubjectCode().toLowerCase());


        if (teacherId == null) {
            request.setStatus("FAILED");
            request.setErrorMess(MessageCode.USER_NOT_FOUND.getCode());
            throw new AmsException(MessageCode.USER_NOT_FOUND);
//            return request;
        }
        if (roomId == null) {
            request.setStatus(Constants.REQUEST_STATUS.FAILED);
            request.setErrorMess(MessageCode.ROOM_NOT_FOUND.getCode());
            throw new AmsException(MessageCode.ROOM_NOT_FOUND);
        }
        if (classId == null) {
            request.setStatus(Constants.REQUEST_STATUS.FAILED);
            request.setErrorMess(MessageCode.CLASS_NOT_FOUND.getCode());
            throw new AmsException(MessageCode.CLASS_NOT_FOUND);
        }
        if (timeslotId == null) {
            request.setStatus(Constants.REQUEST_STATUS.FAILED);
            request.setErrorMess(MessageCode.TIMESLOT_NOT_FOUND.getCode());
            throw new AmsException(MessageCode.TIMESLOT_NOT_FOUND);
        }
        if (subId == null) {
            request.setStatus(Constants.REQUEST_STATUS.FAILED);
            request.setErrorMess(MessageCode.SUBJECT_NOT_FOUND.getCode());
            throw new AmsException(MessageCode.SUBJECT_NOT_FOUND);
        }

        Schedule schedule = scheduleRepository.findById(request.getId()).orElse(null);

        if (schedule == null || schedule.getStatus().equals(Constants.STATUS_TYPE.DELETED)) {
            request.setStatus(Constants.REQUEST_STATUS.FAILED);
            request.setErrorMess(MessageCode.SCHEDULE_NOT_FOUND.getCode());
            return request;
        }
        scheduleList.remove(schedule);
        if (checkExit(sheduleIndexMap, request, timeslotId, roomId, classId, teacherId, subId, schedule.getId()) == null) {
            schedule.setSemester(semester.getId());
            schedule.setUserId(teacherId);
            schedule.setRoomId(roomId);
            schedule.setClassId(classId);
            schedule.setSubjectId(subId);
            schedule.setTimeSlotId(timeslotId);
            schedule.setLearnTimestamp(request.getDate().atStartOfDay());
            schedule.setStatus(Constants.STATUS_TYPE.ACTIVE);
            schedule.setModifiedAt(LocalDateTime.now());
            schedule.setModifiedBy(users.getId());
            schedule.setDescription(request.getDescription());
            request.setStatus(Constants.REQUEST_STATUS.SUCCESS);
            scheduleList.add(schedule);
            scheduleRepository.save(schedule);
        }
        return request;
    }

}
