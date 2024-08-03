package vn.attendance.validate.impl;


import vn.attendance.validate.GioiTinhConstraint;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class GioiTinhValidator implements
        ConstraintValidator<GioiTinhConstraint, Long> {

    @Override
    public void initialize(GioiTinhConstraint gioiTinhConstraint) {
    }

    @Override
    public boolean isValid(Long contactField, ConstraintValidatorContext cxt) {
        if (contactField == null) return true;
        return contactField == 1 || contactField == 0;
    }

}
