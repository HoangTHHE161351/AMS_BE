package vn.attendance.validate;

import vn.attendance.validate.impl.StatusTypeValidator;
import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = StatusTypeValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface StatusTypeConstraint {
    String message() default "Status Type is Invalid";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
