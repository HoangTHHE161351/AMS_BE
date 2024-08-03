package vn.attendance.validate;


import vn.attendance.validate.impl.GioiTinhValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = GioiTinhValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface GioiTinhConstraint {
    String message() default "Giới tính có giá trị 1: nam 0: nữ";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
