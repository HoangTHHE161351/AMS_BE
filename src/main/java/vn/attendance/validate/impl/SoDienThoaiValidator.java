package vn.attendance.validate.impl;

import vn.attendance.util.DataUtil;
import vn.attendance.validate.SoDienThoaiConstraint;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class SoDienThoaiValidator implements
        ConstraintValidator<SoDienThoaiConstraint, String> {

    @Override
    public void initialize(SoDienThoaiConstraint contactNumber) {
    }

    @Override
    public boolean isValid(String s,
                           ConstraintValidatorContext cxt) {
        if (DataUtil.isNullOrEmpty(s)) return true;
        return !DataUtil.isNullOrEmpty(s) && s.matches("[0-9]+")
                && (s.length() > 8) && (s.length() < 14);
    }

}