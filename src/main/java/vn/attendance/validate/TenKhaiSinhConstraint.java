package vn.attendance.validate;


import vn.attendance.validate.impl.TenKhaiSinhValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = TenKhaiSinhValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface TenKhaiSinhConstraint {
    String message() default "Tên khai sinh không đúng đinh dạng";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
