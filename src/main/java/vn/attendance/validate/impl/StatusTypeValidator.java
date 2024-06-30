package vn.attendance.validate.impl;

import vn.attendance.util.Constants;
import vn.attendance.validate.StatusTypeConstraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.lang.reflect.Field;

public class StatusTypeValidator implements
        ConstraintValidator<StatusTypeConstraint, String> {

    @Override
    public void initialize(StatusTypeConstraint  constraintAnnotation) {
    }

    @Override
    public boolean isValid(String status, ConstraintValidatorContext context) {
        try {
            Field[] fields = Constants.STATUS_TYPE.class.getFields();
            for (Field field : fields) {
                if (field.get(null).equals(status)) {
                    return true;
                }
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return false;
    }
}
