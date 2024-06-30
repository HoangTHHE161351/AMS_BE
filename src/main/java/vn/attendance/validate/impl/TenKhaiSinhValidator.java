package vn.attendance.validate.impl;


import vn.attendance.util.DataUtil;
import vn.attendance.validate.TenKhaiSinhConstraint;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class TenKhaiSinhValidator implements ConstraintValidator<TenKhaiSinhConstraint, String> {

    @Override
    public void initialize(TenKhaiSinhConstraint constraintAnnotation) {
    }

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        if (DataUtil.isNullOrEmpty(s)) return true;
//        String patterm = "^[\\w'\\-,.][^0-9_!¡?÷?¿\\/\\\\+=@#$%ˆ&*(){}|~<>;:[\\]]{2,}$";
        s = DataUtil.stripAccents(s);
        String patterm = "^[\\w'\\-,.][^0-9_!¡?÷?¿/\\\\+=@#$%ˆ&*(){}|~<>;:]{2,}$";
        return s.length() <= 150 && s.matches(patterm);
    }
}
