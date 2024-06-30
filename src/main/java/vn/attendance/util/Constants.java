package vn.attendance.util;

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
        String PENDING_ACTIVATION = "PENDING_ACTIVATION";
        String NOT_YET ="NOT YET";
        String ATTEND ="ATTEND";
        String ABSENT ="ABSENT";
    }

    interface ROLE {
        String STUDENT = "STUDENT";
        String TEACHER = "TEACHER";
        String ADMIN = "ADMIN";
        String STAFF = "STAFF";
    }
}
