package vn.attendance.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import vn.attendance.exception.EntityValidationException;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Service
public class ValidationService<T> {

    @Autowired
    private Validator validator;

    @Autowired
    private MessageSource messageSource;

    public void validate(@NotNull T entity) throws EntityValidationException {
        Set<ConstraintViolation<T>> violations = validator.validate(entity);
        Map<String, String> errors = new HashMap<>();

        violations.forEach(violation -> errors.put(violation.getPropertyPath().toString(), violation.getMessage()));
        if (!errors.isEmpty()) {
            throw new EntityValidationException(messageSource.getMessage(
                    "error.validation",
                    new Object[0],
                    LocaleContextHolder.getLocale()), errors);
        }
    }
}
