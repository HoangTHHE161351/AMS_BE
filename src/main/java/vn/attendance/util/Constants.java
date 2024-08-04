package vn.attendance.util;

import jnr.ffi.annotations.In;

public interface Constants {

    public static final String X_TOTAL_COUNT = "X-Total-Count";
    public static final String SUGGEST_CODE = "suggest-code";
    public static final String ERROR_CODE = "error-code";

    public interface COMMON {
        String patternDate = "dd-MM-yyyy";
        String patternXMLDate = "dd-MM-yyyy HH:mm:ss";
        String patternXMLDatetime = "yyyy-MM-dd HH24:MI:ss.FF6";
        String patternXMLDatetime1 = "ddMMyyyyHH24mmss";
        String patternXMLDatetime2 = "yyyyMMddHH24mm";
        String patternSqlDate = "YYYYMMDD";
        String patternDateDanCu = "dd/MM/yyyy";
        String patternSqlDateDC = "yyyyMMdd";
        String patternDateDDMMYYYY = "DDMMYYYY";
        String patternSqlDateFromLocal = "yyyyMMdd";
        Integer THANH_CONG = 1;
        Integer THAT_BAI = 0;
        String CHUA_QUET = "0";
        String DA_QUET = "1";
        String GUI_THAT_BAI = "2";
        String DANG_XU_LY = "4";
        String DA_PHE_DUYET = "5";
        String TU_CHOI = "6";

        String X_TOTAL_COUNT = "X-Total-Count";
        String SUCCESS = "success";
        String ERROR = "err";
        String REPORT_PATH = "jasper/";
        String TEMPLATE = "templates/";
        String LAN_CHAY = "3";
        String TRONG_NGOAI = "TRONG_NGOAI";
        String APP = "APP";
        String INSERT = " INSERT";
    }

    interface MESSAGE {
        String CODE = "Mã bị trùng lặp";

    }

    interface DEVICE_TYPE {
        String CCTV = "CCTV";
        String LCD = "LCD";
        String IVSS = "IVSS";

    }

    interface KAFKA_COMMON {
        String SEND = "SEND";
        String RECEIVE = "RECEIVE";
    }

    interface MESSAGESS_COMMON {
        String ERROR = "ERROR";
        String SUCCESS = "SUCCESS";
    }

    interface STATUS_TYPE {
        String ACTIVE = "ACTIVE";
        String DELETED = "DELETED";
        String INACTIVE = "INACTIVE";
        String PENDING = "PENDING";
        String NOT_YET ="NOT YET";
        String ATTEND ="ATTEND";
        String ABSENT ="ABSENT";
    }

    interface CAMERA_STATE_TYPE {
        String ACTIVE = "ACTIVE";
        String DELETED = "DELETED";
        String PENDING = "PENDING";
        String UNKNOWN ="UNKNOWN";
        String CONNECTING ="CONNECTING";
        String DISCONNECT ="DISCONNECT";
        String ERROR ="ERROR";
    }

    interface REQUEST_STATUS {
        String SUCCESS = "SUCCESS";
        String FAILED = "FAILED";
    }

    interface SERVER_CONNECT_MESSAGE {
        String DISCONNECT = "DISCONNECT";
        String CONNECTED = "CONNECTED";
        String RECONNECT = "RECONNECT";
        String EMPTY = "EMPTY";
    }

    interface ROLE {
        String STUDENT = "STUDENT";
        String TEACHER = "TEACHER";
        String ADMIN = "ADMIN";
        String STAFF = "STAFF";
    }

    interface LCD_OPERATOR {
        String CONTROL_LOG = "RecPush";
        String STRANGER_LOG = "StrSnapPush";
        String ONLINE = "Online";
    }

    interface SERVER_CONNECT {
        String CONNECTED = "CONNECTED";
        String DISCONNECT = "DISCONNECT";
    }

    interface LOG_TYPE {
        Integer UNKNOWN = 0;
        Integer IDENTIFIED = 1;
        Integer UNAUTHORIZED = 2;
        Integer DETECTING = -1;
    }

    interface PERSON_TYPE{
        Integer WHITELIST = 0;
        Integer BLACKLIST = 1;
    }

    interface CHECK_TYPE{
        String IN = "IN";
        String OUT = "OUT";
        String ADD = "ADD";
    }

    interface SCREEN_NAME{
        String LOGS = "LOGS";
        String SYNCHRONIZE = "SYNCHRONIZE";
        String DEVICE = "DEVICE";
    }

    interface NOTIFY_TITLE{
        String STRANGER = "Unknown Person Detected in Room";
        String SYNCHRONIZE_FAIL = "Data Synchronization Failed";
        String SYNCHRONIZE_SUCCESS = "Data Synchronization Successful";
        String DEVICE_DISCONNECT = "Device Disconnect";
        String DEVICE_CONNECT = "Device Disconnect";
    }
}
