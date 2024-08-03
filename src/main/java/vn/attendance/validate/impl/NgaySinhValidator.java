package vn.attendance.validate.impl;


import vn.attendance.util.DataUtil;
import vn.attendance.validate.NgaySinhConstraint;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Objects;

public class NgaySinhValidator implements ConstraintValidator<NgaySinhConstraint, String> {

    @Override
    public void initialize(NgaySinhConstraint constraintAnnotation) {

    }

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        if (DataUtil.isNullOrEmpty(s)) return true;
        String datePattern = "\\d{2}-\\d{2}-\\d{4}";
        if (DataUtil.isNullOrEmpty(s) || !s.matches(datePattern))
            return false;
        return Objects.requireNonNull(DataUtil.convertStringToDate(s, "dd-MM-yyyy")).getTime() < DataUtil.getDate().getTime();
    }

}
