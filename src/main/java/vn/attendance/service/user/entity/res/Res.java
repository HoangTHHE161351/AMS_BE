package vn.attendance.service.user.entity.res;

import lombok.Data;

@Data
public class Res {
    public static final int RESPONSE_SUCCESS = 1;
    public static final int RESPONSE_FAIL = 0;

    private int code;
    private Object data;

    public Res() {
    }

    public Res(int id, Object value) {
        this.code = id;
        this.data = value;
    }

}
