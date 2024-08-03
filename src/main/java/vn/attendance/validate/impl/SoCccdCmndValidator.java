package vn.attendance.validate.impl;


import vn.attendance.util.DataUtil;
import vn.attendance.validate.SoCccdCmndConstraint;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class SoCccdCmndValidator implements
        ConstraintValidator<SoCccdCmndConstraint, String> {

    @Override
    public void initialize(SoCccdCmndConstraint soCccdCmndConstraint) {
    }

    @Override
    public boolean isValid(String s,
                           ConstraintValidatorContext cxt) {
        if (DataUtil.isNullOrEmpty(s)) return true;
//        return s.matches("[0-9]+")
//                && ((s.length() == 9) || (s.length() == 12));
        return true;
    }

}
