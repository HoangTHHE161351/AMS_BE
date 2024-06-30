package vn.attendance.exception;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

/**
 * NotFound Exception
 */
@Getter
@Setter
public class NotFoundException extends Exception {

    public static final String ERROR_USER_NOT_FOUND = "ERROR_USER_NOT_FOUND";
    public static final String ERROR_COURSE_NOT_FOUND = "ERROR_COURSE_NOT_FOUND";
    public static final String ERROR_UNIT_TYPE_NOT_FOUND = "ERROR_UNIT_TYPE_NOT_FOUND";
    public static final String ERROR_UNIT_COURSE_NOT_FOUND = "ERROR_UNIT_COURSE_NOT_FOUND";
    public static final String ERROR_UNIT_TEST_USER_NOT_FOUND = "ERROR_UNIT_TEST_USER_NOT_FOUND";
    public static final String ERROR_UNIT_LESSON_OF_USER_NOT_FOUND = "ERROR_UNIT_LESSON_OF_USER_NOT_FOUND";
    public static final String ERROR_UNIT_QUESTION_NOT_FOUND = "ERROR_UNIT_QUESTION_NOT_FOUND";
    public static final String ERROR_NOTIFICATION_NOT_FOUND = "ERROR_NOTIFICATION_NOT_FOUND";
    public static final String ERROR_RESULT_QUESTION_TEST_NOT_FOUND = "ERROR_RESULT_QUESTION_TEST_NOT_FOUND";
    public static final String ERROR_UNIT_REPORT_USER_NOT_FOUND = "ERROR_UNIT_REPORT_USER_NOT_FOUND";
    public static final String ERROR_USER_UNIT_SURVEY_NOT_FOUND = "ERROR_USER_UNIT_SURVEY_NOT_FOUND";
    public static final String ERROR_USER_COURSE_NOT_FOUND = "ERROR_USER_COURSE_NOT_FOUND";
    public static final String ERROR_UNIT_SURVEY_USER_NOT_FOUND = "ERROR_UNIT_SURVEY_USER_NOT_FOUND";
    public static final String ERROR_RESULT_QUESTION_SURVEY_NOT_FOUND = "ERROR_RESULT_QUESTION_SURVEY_NOT_FOUND";
    public static final String ERROR_S3_BUCKET_NOT_FOUND = "ERROR_S3_BUCKET_NOT_FOUND";
    public static final String ERROR_RESULT_UNIT_NOT_FOUND = "ERROR_RESULT_UNIT_NOT_FOUND";
    public static final String ERROR_UNIT_SURVEY_NOT_FOUND = "ERROR_UNIT_SURVEY_NOT_FOUND";
    public static final String ERROR_COURSE_FILE_NOT_FOUND = "ERROR_COURSE_FILE_NOT_FOUND";
    public static final String ERROR_MAIL_NOT_FOUND = "ERROR_MAIL_NOT_FOUND";
    public static final String ERROR_NEWS_NOT_FOUND = "ERROR_NEWS_NOT_FOUND";
    public static final String ERROR_ATTRIBUTE_NOT_FOUND = "ERROR_ATTRIBUTE_NOT_FOUND";
    public static final String ERROR_COMPANY_NOT_FOUND = "ERROR_COMPANY_NOT_FOUND";

    public static final String ERROR_COMPANY_CODE_SERAKU_NULL = "ERROR_COMPANY_CODE_SERAKU_NULL";
    public static final String ERROR_SEMINAR_NOT_FOUND = "ERROR_SEMINAR_NOT_FOUND";
    public static final String ERROR_UNIT_QUESTION_CATEGORY_NOT_FOUND = "ERROR_UNIT_QUESTION_CATEGORY_NOT_FOUND";
    public static final String ERROR_DEPARTMENT_NOT_FOUND = "ERROR_DEPARTMENT_NOT_FOUND";
    public static final String ERROR_AUTOMATIC_COURSE_ASSIGNMENT_SETTING_NOT_FOUND = "ERROR_AUTOMATIC_COURSE_ASSIGNMENT_SETTING_NOT_FOUND";
    public static final String ERROR_VIDEO_NOT_FOUND = "ERROR_VIDEO_NOT_FOUND";
    public static final String ERROR_COMPANY_THEME_NOT_FOUND = "ERROR_COMPANY_THEME_NOT_FOUND";

    //    public static final String ERROR_UNIT_SURVEY_NOT_FOUND = "ERROR_UNIT_SURVEY_NOT_FOUND";
    public static final String ERROR_UNIT_LESSON_NOT_FOUND = "ERROR_UNIT_LESSON_NOT_FOUND";
    public static final String ERROR_UNIT_REPORT_NOT_FOUND = "ERROR_UNIT_REPORT_NOT_FOUND";
    public static final String ERROR_UNIT_TEST_NOT_FOUND = "ERROR_UNIT_TEST_NOT_FOUND";
    public static final String ERROR_CART_NOT_FOUND = "ERROR_CART_NOT_FOUND";
    public static final String ERROR_FOLDER_NOT_FOUND = "ERROR_FOLDER_NOT_FOUND";
    public static final String ERROR_STATUS_NOT_FOUND = "ERROR_STATUS_NOT_FOUND";
    public static final String ERROR_GROUP_NOT_FOUND = "ERROR_GROUP_NOT_FOUND";
    public static final String ERROR_PLAN_PACKAGE_NOT_FOUND = "ERROR_PLAN_PACKAGE_NOT_FOUND";
    public static final String ERROR_MAINTENANCE_SETTING_NOT_FOUND = "ERROR_MAINTENANCE_SETTING_NOT_FOUND";
    public static final String ERROR_PROJECT_NOT_FOUND = "ERROR_PROJECT_NOT_FOUND";
    public static final String ERROR_TAG_NOT_FOUND = "ERROR_TAG_NOT_FOUND";
    public static final String ERROR_USER_TALK_BOARD_NOT_FOUND = "ERROR_USER_TALK_BOARD_NOT_FOUND";
    public static final String ERROR_TALK_BOARD_NOT_FOUND = "ERROR_TALK_BOARD_NOT_FOUND";
    public static final String ERROR_USER_TALK_BOARD_COMMENT_NOT_FOUND = "ERROR_USER_TALK_BOARD_COMMENT_NOT_FOUND";
    public static final String ERROR_TALK_BOARD_COMMENT_NOT_FOUND = "ERROR_TALK_BOARD_COMMENT_NOT_FOUND";
    public static final String ERROR_FILE_NOT_FOUND = "ERROR_FILE_NOT_FOUND";
    public static final String ERROR_USER_NOT_EXIST_FIRESTORE = "ERROR_USER_NOT_EXIST_FIRESTORE";
    public static final String ERROR_GROUP_NOT_EXIST_FIRESTORE = "ERROR_GROUP_NOT_EXIST_FIRESTORE";
    public static final String ERROR_CONFIG_SEND_EMAIL_NOT_FOUND = "ERROR_CONFIG_SEND_EMAIL_NOT_FOUND";
    public static final String REPORT_NOT_FOUND = "DAILY_REPORT_NOT_FOUND";
    public static final String REPORT_COMMENT_NOT_FOUND = "REPORT_COMMENT_NOT_FOUND";
    public static final String ERROR_HISTORY_USER_COURSE_UNIT_NOT_FOUND = "ERROR_HISTORY_USER_COURSE_UNIT_NOT_FOUND";
    public static final String ERROR_MOBILE_SETTING_NOT_FOUND = "ERROR_MOBILE_SETTING_NOT_FOUND";

    public static final String USER_COURSE_UNITS_NOT_FOUND = "USER_COURSE_UNITS_NOT_FOUND";
    public static final String VARIABLE_NOT_FOUND = "VARIABLE_NOT_FOUND";

    public static final String ERROR_EXTERNAl_API_MANAGER_NOT_FOUND = "ERROR_EXTERNAl_API_MANAGER_NOT_FOUND";
    public static final String ERROR_EXTERNAl_API_NOT_FOUND = "ERROR_EXTERNAl_API_NOT_FOUND";
    public static final String ERROR_IP_CONFIGURATION_MANAGER_NOT_FOUND = "ERROR_IP_CONFIGURATION_MANAGER_NOT_FOUND";

    private static final long serialVersionUID = 1L;
    private String error;
    private String message;
    private HttpStatus httpStatus;
    @JsonIgnore
    private boolean isPrintStackTrace;

    public NotFoundException() {
        super();
    }

    public NotFoundException(String error, String message) {
        super(message);
        this.error = error;
        this.message = message;
        this.httpStatus = HttpStatus.NOT_FOUND;
    }

    public NotFoundException(String error, String message, HttpStatus httpStatus) {
        super(message);
        this.error = error;
        this.message = message;
        this.httpStatus = httpStatus;
    }
}
