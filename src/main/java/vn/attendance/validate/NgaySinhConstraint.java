package vn.attendance.validate;


import vn.attendance.validate.impl.NgaySinhValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = NgaySinhValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface NgaySinhConstraint {
    String message() default "Ngày sinh trống hoặc sau ngày hiện tại";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
