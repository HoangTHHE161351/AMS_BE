package vn.attendance.service.student;

import org.apache.commons.io.output.ByteArrayOutputStream;
import org.apache.poi.hssf.usermodel.HSSFPicture;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.attendance.config.authen.BaseUserDetailsService;
import vn.attendance.exception.AmsException;
import vn.attendance.model.ClassRoom;
import vn.attendance.model.ClassSubject;
import vn.attendance.model.StudentCurriculum;
import vn.attendance.model.Users;
import vn.attendance.repository.ClassRoomRepository;
import vn.attendance.repository.ClassSubjectRepository;
import vn.attendance.repository.StudentCurriculumRepository;
import vn.attendance.repository.StudentRepository;
import vn.attendance.service.student.request.AddStudentCurriculum;
import vn.attendance.service.student.request.EditStudentCurriculum;
import vn.attendance.service.student.response.IDropdownStudentDto;
import vn.attendance.service.student.response.StudentCurriculumDto;
import vn.attendance.service.student.response.StudentDto;
import vn.attendance.service.student.service.StudentService;
import vn.attendance.service.user.service.response.UsersDto;
import vn.attendance.util.Constants;
import vn.attendance.util.MessageCode;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class StudentServiceImpl implements StudentService {

    @Autowired
    StudentRepository studentRepository;
    @Autowired
    StudentCurriculumRepository studentCurriculumRepository;
    @Autowired
    ClassRoomRepository classRoomRepository;
    @Autowired
    ClassSubjectRepository classSubjectRepository;

    @Override
    public byte[] exportStudent(String search, String status, String curriculumName) throws AmsException {
        HSSFWorkbook workbook = new HSSFWorkbook();

        try {
            List<StudentDto> users = studentRepository.findStudentByConditions(search, status, curriculumName);
            if (users.size() <= 0) {
                throw new AmsException(MessageCode.CONTENT_EMPTY);
            }
            Sheet sheet = workbook.createSheet("Student List");
            // Tạo tiêu đề cho các cột
            Row headerRow = sheet.createRow(0);

            // Create headers
            String[] headers = {"No", "User Name", "Full Name", "Curriculum", "Email", "Avatar", "Gender", "Address", "Phone", "Date of Birth", "Role", "Status", "Created By", "Created At", "Modified By", "Modified At"};
            for (int i = 0; i < headers.length; i++) {
                headerRow.createCell(i).setCellValue(headers[i]);
            }

            int rowNum = 1;
            for (StudentDto user : users) {
                Row dataRow = sheet.createRow(rowNum++);

                dataRow.createCell(0).setCellValue(rowNum - 1);
                dataRow.createCell(1).setCellValue(user.getUsername());
                dataRow.createCell(2).setCellValue(user.getFullname());
                dataRow.createCell(3).setCellValue(user.getCurriculumName() != null ? user.getCurriculumName() : "N/A");
                dataRow.createCell(4).setCellValue(user.getEmail());

                // Handle avatar image
                if (user.getAvata() != null) {
                    try {
                        byte[] imageBytes = Base64.getDecoder().decode(user.getAvata());
                        addImageToSheet(sheet, imageBytes, rowNum - 1, 4, workbook);
                    } catch (IllegalArgumentException | IOException e) {
                        dataRow.createCell(5).setCellValue("N/A");
                    }
                } else {
                    dataRow.createCell(5).setCellValue("N/A");
                }

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

                // Check and handle null values
                dataRow.createCell(6).setCellValue(user.getGender() == 1 ? "MALE" : "FEMALE");
                dataRow.createCell(7).setCellValue(Objects.requireNonNullElse(user.getAddress(), "N/A"));
                dataRow.createCell(8).setCellValue(Objects.requireNonNullElse(user.getPhone(), "N/A"));
                dataRow.createCell(9).setCellValue(user.getDob() != null ? user.getDob().format(formatter) : "N/A");
                dataRow.createCell(10).setCellValue(Objects.requireNonNullElse(user.getStatus(), "N/A"));
                dataRow.createCell(11).setCellValue(user.getCreatedBy() != null ? user.getCreatedBy() : "N/A");
                dataRow.createCell(12).setCellValue(user.getCreatedAt() != null ? user.getCreatedAt().format(formatter) : "N/A");
                dataRow.createCell(13).setCellValue(user.getModifiedBy() != null ? user.getModifiedBy() : "N/A");
                dataRow.createCell(14).setCellValue(user.getModifiedAt() != null ? user.getModifiedAt().format(formatter) : "N/A");
            }


            ByteArrayOutputStream out = new ByteArrayOutputStream();
            workbook.write(out);
            workbook.close();

            byte[] fileContent = out.toByteArray();

            return fileContent;
        } catch (IOException e) {
            throw new AmsException(MessageCode.EXPORT_FAIL);
        } finally {
            try {
                workbook.close();
            } catch (IOException e) {
                throw new AmsException(MessageCode.EXPORT_FAIL);
            }
        }
    }

    @Override
    public StudentDto getStudentById(Integer id) {
        Optional<StudentDto> studentDto = studentRepository.findStudentById(id);
        return studentDto.isPresent() ? studentDto.get() : null;
    }



    @Override
    public List<String> getStudentTimeline(Integer id, LocalDate date) {
        return List.of();
    }

    private void addImageToSheet(Sheet sheet, byte[] imageBytes, int rowNumber, int columnNumber, Workbook workbook) throws IOException {
        int pictureIdx = workbook.addPicture(imageBytes, Workbook.PICTURE_TYPE_PNG);
        CreationHelper helper = workbook.getCreationHelper();
        Drawing<?> drawing = sheet.createDrawingPatriarch();

        ClientAnchor anchor = helper.createClientAnchor();
        anchor.setCol1(columnNumber);
        anchor.setRow1(rowNumber);
        anchor.setCol2(columnNumber + 1);
        anchor.setRow2(rowNumber + 1);

        HSSFPicture picture = (HSSFPicture) drawing.createPicture(anchor, pictureIdx);
        picture.resize(1.0); // Resize image to fit the cell
    }

    @Override
    public Page<Users> findStudent(int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);

        return studentRepository.findStudent(pageable);
    }

    @Override
    public Page<StudentDto> searchStudent(String search, String curriculumName, String status, int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        return studentRepository.findStudentby(search, status, curriculumName, pageable);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public AddStudentCurriculum addStudentCurriculum(AddStudentCurriculum request) throws AmsException {
        Users user = BaseUserDetailsService.USER.get();
        if (user == null) {
            throw new AmsException(MessageCode.USER_NOT_FOUND);
        }

        Optional<StudentDto> student = studentRepository.findStudentById(request.getStudentId());

        if (!student.isPresent()) {
            request.setStatus(Constants.REQUEST_STATUS.FAILED);
            request.setErrorMess(MessageCode.STUDENT_NOT_FOUND.toString());
            throw new AmsException(MessageCode.STUDENT_NOT_FOUND);
        }

        if (student.get().getCurriculumName() != null) {
            request.setStatus(Constants.REQUEST_STATUS.FAILED);
            request.setErrorMess(MessageCode.STUDENT_ALREADY_HAVE_CURRICULUM.toString());
            throw new AmsException(MessageCode.STUDENT_ALREADY_HAVE_CURRICULUM);
        }

        StudentCurriculum studentCurriculum = new StudentCurriculum();
        studentCurriculum.setStudentId(request.getStudentId());
        studentCurriculum.setCurriculumId(request.getCourseId());
        studentCurriculum.setDescription(request.getDescription());
        studentCurriculum.setStatus(Constants.STATUS_TYPE.ACTIVE);
        studentCurriculum.setCreatedAt(LocalDateTime.now());
        studentCurriculum.setCreatedBy(user.getId());

        request.setStatus(Constants.REQUEST_STATUS.SUCCESS);

        studentCurriculumRepository.save(studentCurriculum);

        return request;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public EditStudentCurriculum updateStudentCurriculum(Integer id, EditStudentCurriculum request) throws AmsException {
        Users users = BaseUserDetailsService.USER.get();
        if (users == null) {
            throw new AmsException(MessageCode.USER_NOT_FOUND);
        }

        StudentCurriculum oldStudentCurriculum = studentCurriculumRepository.findById(id).orElse(null);

        if (oldStudentCurriculum == null || oldStudentCurriculum.getStatus().equals(Constants.STATUS_TYPE.DELETED)) {
            throw new AmsException(MessageCode.CURRICULUM_NOT_FOUND);
        }

        if (oldStudentCurriculum.getCurriculumId() == request.getCourseId() &&
                oldStudentCurriculum.getStatus().equals(Constants.STATUS_TYPE.ACTIVE)) {
            request.setStatus(Constants.REQUEST_STATUS.FAILED);
            request.setErrorMess(MessageCode.STUDENT_ALREADY_HAVE_CURRICULUM.toString());
            return request;
        } else {
            oldStudentCurriculum.setStatus(Constants.STATUS_TYPE.DELETED);
            StudentCurriculum newStudentCurriculum = new StudentCurriculum();

            newStudentCurriculum.setStudentId(oldStudentCurriculum.getStudentId());
            newStudentCurriculum.setCurriculumId(request.getCourseId());
            newStudentCurriculum.setDescription(request.getDescription());
            newStudentCurriculum.setStatus(Constants.STATUS_TYPE.ACTIVE);
            newStudentCurriculum.setModifiedAt(LocalDateTime.now());
            newStudentCurriculum.setModifiedBy(users.getId());
            request.setStatus(Constants.REQUEST_STATUS.SUCCESS);

            studentCurriculumRepository.save(oldStudentCurriculum);
            studentCurriculumRepository.save(newStudentCurriculum);
        }
        return request;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Users deleteStudentCurriculum(Integer id) throws AmsException{
        Users user = BaseUserDetailsService.USER.get();
        if (user == null) {
            throw new AmsException(MessageCode.USER_NOT_FOUND);
        }

        Users student = studentRepository.getStudentById(id);
        if (student==null)throw new AmsException(MessageCode.STUDENT_NOT_FOUND);
        StudentCurriculum studentCurriculum = studentCurriculumRepository.findByStudentId(id);
        if (studentCurriculum!=null){
            studentCurriculum.setStatus(Constants.STATUS_TYPE.DELETED);
            studentCurriculum.setModifiedAt(LocalDateTime.now());
            studentCurriculum.setModifiedBy(user.getId());
            studentCurriculumRepository.save(studentCurriculum);
        }

        student.setStatus(Constants.STATUS_TYPE.DELETED);
        student.setModifiedAt(LocalDateTime.now());
        student.setModifiedBy(user.getId());

        return studentRepository.save(student);
    }

    @Override
    public Page<StudentCurriculumDto> searchStudentCurriculum(String search, int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        return studentRepository.findStudentCurriculum(search, pageable);
    }

    @Override
    public List<IDropdownStudentDto> dropdownStudent(String search){
        return studentRepository.dropdownStudent(search);
    }

    @Override
    public List<IDropdownStudentDto> dropdownStudentToClass(String search, Integer classId) {
        return studentRepository.dropdownStudentToClass(search, classId);
    }
}
