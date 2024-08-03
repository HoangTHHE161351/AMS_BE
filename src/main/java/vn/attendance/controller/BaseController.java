package vn.attendance.controller;

import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.beanutils.ConvertUtilsBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import vn.attendance.exception.EntityValidationException;
import vn.attendance.service.ValidationService;
import vn.attendance.service.user.facade.LoginFacade;
import vn.attendance.util.DataUtil;

import java.lang.reflect.InvocationTargetException;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

@Service
public class BaseController {
    private static final String[] ignoredProps = new String[]{"createdBy", "createdAt", "modifiedBy", "modifiedAt"};
    protected final BeanUtilsBean beanUtilsBean = new BeanUtilsBean(new ConvertUtilsBean() {
        @Override
        public Object convert(String value, Class clazz) {
            if (clazz.equals(UUID.class)) {
                return UUID.fromString(value);
            } else {
                return super.convert(value, clazz);
            }
        }
    });
    @Autowired
    protected MessageSource messageSource;
    @Autowired
    protected MessageSource validationMessageResource;
    @Autowired
    protected ValidationService validationService;
    @Autowired
    protected LoginFacade loginFacade;

    protected String messageTemplate(String key) {
        return messageSource.getMessage(key, new Object[0], LocaleContextHolder.getLocale());
    }

    protected String messageTemplate(String key, String param, Locale locale) {
        Object[] a = {param};
        return messageSource.getMessage(key, a, locale);
    }

    protected <T> void validate(T object) throws EntityValidationException {
        validationService.validate(object);
    }

    protected void copyProperties(Object obj1, Object obj2) {
        org.springframework.beans.BeanUtils.copyProperties(obj1, obj2);
    }

    protected void copyPropertiesIgnoreProp(Object obj1, Object obj2) {
        org.springframework.beans.BeanUtils.copyProperties(obj1, obj2, ignoredProps);
    }

    protected Long getUserId(Map<String, String> headers) throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        return DataUtil.safeToLong(beanUtilsBean.getProperty(loginFacade.getProfileUser(headers).getData(), "id"));
    }
}
