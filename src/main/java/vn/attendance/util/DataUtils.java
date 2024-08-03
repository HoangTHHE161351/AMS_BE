package vn.attendance.util;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.CharMatcher;
import com.google.common.base.Strings;
import com.google.common.base.Supplier;
import com.google.common.collect.Lists;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.validator.routines.DateValidator;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Pageable;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Blob;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.Normalizer;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Slf4j
public class DataUtils {


    private static final String[] CHARLIST_UNICODE = {"à", "á", "ả", "ã", "ạ", "â", "ầ", "ấ", "ẩ", "ẫ", "ậ", "ă", "ằ", "ắ", "ẳ", "ẵ", "ặ",
            "è", "é", "ẻ", "ẽ", "ẹ", "ê", "ề", "ế", "ể", "ễ", "ệ",
            "ì", "í", "ỉ", "ĩ", "ị",
            "ò", "ó", "ỏ", "õ", "ọ", "ô", "ồ", "ố", "ổ", "ỗ", "ộ", "ơ", "ờ", "ớ", "ở", "ỡ", "ợ",
            "ù", "ú", "ủ", "ũ", "ụ", "ư", "ừ", "ứ", "ử", "ữ", "ự",
            "ỳ", "ý", "ỷ", "ỹ", "ỵ",
            "À", "Á", "Ả", "Ã", "Ạ", "Â", "Ầ", "Ấ", "Ẩ", "Ẫ", "Ậ", "Ă", "Ằ", "Ắ", "Ẳ", "Ẵ", "Ặ",
            "È", "É", "Ẻ", "Ẽ", "Ẹ", "Ê", "Ề", "Ế", "Ể", "Ễ", "Ệ",
            "Ì", "Í", "Ỉ", "Ĩ", "Ị",
            "Ò", "Ó", "Ỏ", "Õ", "Ọ", "Ô", "Ồ", "Ố", "Ổ", "Ỗ", "Ộ", "Ơ", "Ờ", "Ớ", "Ở", "Ỡ", "Ợ",
            "Ù", "Ú", "Ủ", "Ũ", "Ụ", "Ư", "Ừ", "Ứ", "Ử", "Ữ", "Ự", "Ỳ", "Ý", "Ỷ", "Ỹ", "Ỵ"};
    private static final String[] CHARLIST_UF8 = {"à", "á", "ả", "ã", "ạ", "â", "ầ", "ấ", "ẩ", "ẫ", "ậ", "ă", "ằ", "ắ", "ẳ", "ẵ", "ặ",
            "è", "é", "ẻ", "ẽ", "ẹ", "ê", "ề", "ế", "ể", "ễ", "ệ",
            "ì", "í", "ỉ", "ĩ", "ị",
            "ò", "ó", "ỏ", "õ", "ọ", "ô", "ồ", "ố", "ổ", "ỗ", "ộ", "ơ", "ờ", "ớ", "ở", "ỡ", "ợ",
            "ù", "ú", "ủ", "ũ", "ụ", "ư", "ừ", "ứ", "ử", "ữ", "ự",
            "ý", "ỳ", "ỷ", "ỹ", "ỵ",
            "À", "Á", "Ả", "Ã", "Ạ", "Â", "Ầ", "Ấ", "Ẩ", "Ẫ", "Ậ", "Ă", "Ằ", "Ắ", "Ẳ", "Ẵ", "Ặ",
            "È", "É", "Ẻ", "Ẽ", "Ẹ", "Ê", "Ề", "Ế", "Ể", "Ễ", "Ệ",
            "Ì", "Í", "Ỉ", "Ĩ", "Ị",
            "Ò", "Ó", "Ỏ", "Õ", "Ọ", "Ô", "Ồ", "Ố", "Ổ", "Ỗ", "Ộ", "Ơ", "Ờ", "Ớ", "Ở", "Ỡ", "Ợ",
            "Ù", "Ú", "Ủ", "Ũ", "Ụ", "Ư", "Ừ", "Ứ", "Ử", "Ữ", "Ự", "Ý", "Ỳ", "Ỷ", "Ỹ", "Ỵ"};

    public static Date getDate() {
        LocalDateTime now = LocalDateTime.now();
        ZonedDateTime zonedDateTime = now.atZone(ZoneId.systemDefault());
        return Date.from(zonedDateTime.toInstant());
    }

    public static LocalDateTime getLocalDateTime() {

        return LocalDateTime.now();
    }

    public static boolean isValidEmail(String email) {
        String emailRegex = "[A-Z0-9a-z\\._%+-]+@([A-Za-z0-9-]+\\.)+[A-Za-z]{2,4}";
        return email.matches(emailRegex);
    }

    public static LocalDate convertToLocalDateViaMilisecond(Date dateToConvert) {
        return Instant.ofEpochMilli(dateToConvert.getTime())
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
    }

    public static boolean isValidMutilEmail(String email) {
        if (DataUtils.isNullObject(email)) {
            return false;
        }
        String[] input = email.split(";");
        if (!DataUtils.isNullOrEmpty(input) && input.length > 0) {
            for (int i = 0; i < input.length; i++) {
                if (!isValidEmail(input[i])) {
                    return false;
                }
            }
        }
        return true;
    }

    public static boolean isValidPhoneNumber(String phoneNumber) {
        return phoneNumber.matches("^\\s*(?:\\+?(\\d{1,3}))?[- (]*(\\d{3})[- )]*(\\d{3})[- ]*(\\d{4})(?: *[x/#]{1}(\\d+))?\\s*$");
    }

    // check nhieu so dien thoai
    public static boolean isValidMultiPhoneNumber(String phoneNumber) {
        if (DataUtils.isNullOrEmpty(phoneNumber)) {
            return false;
        }
        String[] input = phoneNumber.split(";");
        if (!DataUtils.isNullOrEmpty(input) && input.length > 0) {
            for (int i = 0; i < input.length; i++) {
                if (!isValidPhoneNumber(input[i])) {
                    return false;
                }
            }
        }
        return true;
    }

    public static String addZeroToString(String input, int strLength) {
        String result = input;
        for (int i = 1; i <= strLength - input.length(); i++) {
            result = "0" + result;
        }
        return result;
    }

    /**
     * Copy du lieu tu bean sang bean moi
     * Luu y chi copy duoc cac doi tuong o ngoai cung, list se duoc copy theo tham chieu
     * <p>
     * Chi dung duoc cho cac bean java, khong dung duoc voi cac doi tuong dang nhu String, Integer, Long...
     *
     * @param source
     * @param <T>
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T> T cloneBean(T source) {
        try {
            if (source == null) {
                return null;
            }
            T dto = (T) source.getClass().getConstructor().newInstance();
            BeanUtils.copyProperties(source, dto);
            return dto;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return null;
        }
    }

    /**
     * Copy 1 list bean sang list moi
     *
     * @param sourceList
     * @param <T>
     * @return
     */
    public static <T> List<T> cloneBean(Iterable<T> sourceList) {
        final List<T> destList = Lists.newArrayList();

        if (sourceList == null) {
            return destList;
        }

        sourceList.forEach(t -> {
            T destObject = cloneBean(t);
            if (destObject != null) {
                destList.add(cloneBean(t));
            }
        });

        return destList;
    }

    /**
     * Clone an object completely
     *
     * @param source
     * @param <T>
     * @return
     * @author KhuongDV
     */
    @SuppressWarnings("unchecked")
    public static <T> T deepCloneObject(T source) {
        try {
            if (source == null) {
                return null;
            }
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream out = new ObjectOutputStream(bos);
            out.writeObject(source);
            out.flush();
            out.close();

            ObjectInputStream in = new ObjectInputStream(
                    new ByteArrayInputStream(bos.toByteArray()));
            T dto = (T) in.readObject();
            in.close();
            return dto;
        } catch (IOException | ClassNotFoundException e) {
            log.error(e.getMessage(), e);
            return null;
        }
    }

    /**
     * forward page
     *
     * @return
     * @author ThanhNT
     */
    public static String forwardPage(String pageName) {
        if (!DataUtils.isNullOrEmpty(pageName)) {
            return "pretty:" + pageName.trim();
        }
        return "";
    }

    /*
     * Kiem tra Long bi null hoac zero
     *
     * @param value
     * @return
     */
    public static boolean isNullOrZero(Long value) {
        return (value == null || value.equals(0L));
    }

    /*
     * Kiem tra Long bi null hoac zero
     *
     * @param value
     * @return
     */
    public static boolean isNullOrZero(Double value) {
        return (value == null || value == 0);
    }

    /*
     * Kiem tra Long bi null hoac zero
     *
     * @param value
     * @return
     */
    public static boolean isNullOrZero(String value) {
        return (value == null || value.trim().equals(""));
    }

    /*
     * Kiem tra Long bi null hoac zero
     *
     * @param value
     * @return
     */
    public static boolean isNullOrZero(Integer value) {
        return (value == null || value.equals(0));
    }

    /*
     * Kiem tra Long bi null hoac zero
     *
     * @param value
     * @return
     */
    public static boolean isNullOrOneNavigate(Long value) {
        return (value == null || value.equals(-1L));
    }

    public static boolean isNullOrOneNavigate(Integer value) {
        return (value == null || value.equals(-1));
    }

    // hungnn, check BigInteger
    public static boolean isNullOrOneBigInteger(BigInteger value) {
        return (value == null || value.equals(-1));
    }

    public static boolean isNullOrNotGreaterZero(Long value) {
        return (value == null || value.compareTo(0L) <= 0);
    }

    /**
     * Kiem tra Bigdecimal bi null hoac zero
     *
     * @param value
     * @return
     */
    public static boolean isNullOrZero(BigDecimal value) {
        return (value == null || value.compareTo(BigDecimal.ZERO) == 0);
    }

    /**
     * Upper first character
     *
     * @param input
     * @return
     */
    public static String upperFirstChar(String input) {
        if (DataUtils.isNullOrEmpty(input)) {
            return input;
        }
        return input.substring(0, 1).toUpperCase() + input.substring(1);
    }

    /**
     * Lower first characater
     *
     * @param input
     * @return
     */
    public static String lowerFirstChar(String input) {
        if (DataUtils.isNullOrEmpty(input)) {
            return input;
        }
        return input.substring(0, 1).toLowerCase() + input.substring(1);
    }

    public static String stripAccents(String input) {
        if (DataUtils.isNullOrEmpty(input)) {
            return input;
        }
        input = StringUtils.stripAccents(input);
        input = input.replace("Đ", "D").replace("Ð", "D").replace("ð", "d");
        return input.replace("đ", "d");
    }

    public static String safeTrim(String obj) {
        if (obj == null) return null;
        return obj.trim();
    }

    public static String safeToUpperCase(String obj) {
        if (obj == null) return null;
        return obj.toUpperCase();
    }

    public static Long safeToLong(Object obj1, Long defaultValue) {
        if (obj1 == null) {
            return defaultValue;
        }
        if (obj1 instanceof BigDecimal) {
            return ((BigDecimal) obj1).longValue();
        }
        if (obj1 instanceof BigInteger) {
            return ((BigInteger) obj1).longValue();
        }

        try {
            return Long.parseLong(obj1.toString());
        } catch (final NumberFormatException nfe) {
            return defaultValue;
        }
    }

    /**
     * @param obj1 Object
     * @return Long
     */
    public static Long safeToLong(Object obj1) {
        return safeToLong(obj1, 0L);
    }

    public static Double safeToDouble(Object obj1, Double defaultValue) {
        if (obj1 == null) {
            return defaultValue;
        }
        try {
            return Double.parseDouble(obj1.toString());
        } catch (final NumberFormatException nfe) {
            return defaultValue;
        }
    }

    public static Double safeToDouble(Object obj1) {
        return safeToDouble(obj1, 0.0);
    }

    public static Short safeToShort(Object obj1, Short defaultValue) {
        if (obj1 == null) {
            return defaultValue;
        }
        try {
            return Short.parseShort(obj1.toString());
        } catch (final NumberFormatException nfe) {
            return defaultValue;
        }
    }

    public static Short safeToShort(Object obj1) {
        return safeToShort(obj1, (short) 0);
    }

    /**
     * @param obj1
     * @param defaultValue
     * @return
     * @author phuvk
     */
    public static int safeToInt(Object obj1, Integer defaultValue) {
        if (obj1 == null) {
            return defaultValue;
        }
        try {
            return Integer.parseInt(obj1.toString());
        } catch (final NumberFormatException nfe) {
            return defaultValue;
        }
    }

    /**
     * @param obj1 Object
     * @return int
     */
    public static int safeToInt(Object obj1) {
        return safeToInt(obj1, 0);
    }

    /**
     * @param obj1 Object
     * @return String
     */
    public static String safeToString(Object obj1, String defaultValue) {
        if (obj1 == null) {
            return defaultValue;
        }

        return obj1.toString();
    }

    public static String safeToLower(String obj1) {
        if (obj1 == null) {
            return null;
        }

        return obj1.toLowerCase();
    }

    /**
     * @param obj1 Object
     * @return String
     */

    public static String safeToStringNull(Object obj1) {
        if (null == safeToString(obj1) || "".equals(safeToString(obj1))) {
            return null;
        } else {
            return safeToString(obj1);
        }

    }

    public static String safeToString(Object obj1) {
        return safeToString(obj1, "");
    }

    public static Date safeToDate(Object obj1) {
        if (obj1 == null) {
            return null;
        }
        return (Date) obj1;
    }

    public static java.sql.Date safeToSqlDate(Object obj1) {
        if (obj1 == null) {
            return null;
        }
        return (java.sql.Date) obj1;
    }

    public static java.sql.Timestamp safeToSqlTimestamp(Object obj1) {
        if (obj1 == null) {
            return null;
        }
        return (java.sql.Timestamp) obj1;
    }

    public static LocalDate safeToLocalDate(Object obj1) {
        if (obj1 == null) {
            return null;
        }
        return convertToLocalDate(safeToDate(obj1));
    }

    /**
     * safe equal
     *
     * @param obj1 Long
     * @param obj2 Long
     * @return boolean
     */
    public static boolean safeEqual(Long obj1, Long obj2) {
        if (obj1 == obj2) return true;
        return ((obj1 != null) && (obj2 != null) && (obj1.compareTo(obj2) == 0));
    }
    public static boolean safeEqual(Integer obj1, Integer obj2) {
        if (obj1 == obj2) return true;
        return ((obj1 != null) && (obj2 != null) && (obj1.compareTo(obj2) == 0));
    }

    /**
     * safe equal
     *
     * @param obj1 Long
     * @param obj2 Long
     * @return boolean
     */
    public static boolean safeEqual(BigInteger obj1, BigInteger obj2) {
        if (obj1 == obj2) return true;
        return (obj1 != null) && (obj2 != null) && obj1.equals(obj2);
    }

    /**
     * @param obj1
     * @param obj2
     * @return
     * @date 09-12-2015 17:43:20
     * @author TuyenNT17
     * @description
     */
    public static boolean safeEqual(Short obj1, Short obj2) {
        if (obj1 == obj2) return true;
        return ((obj1 != null) && (obj2 != null) && (obj1.compareTo(obj2) == 0));
    }

    /**
     * safe equal
     *
     * @param obj1 String
     * @param obj2 String
     * @return boolean
     */
    public static boolean safeEqual(String obj1, String obj2) {
        if (obj1 == obj2) return true;
        return ((obj1 != null) && (obj2 != null) && obj1.equals(obj2));
    }

    /**
     * safe equal
     *
     * @param obj1 String
     * @param obj2 String
     * @return boolean
     */
    public static boolean safeEqualIgnoreCase(String obj1, String obj2) {
        if (obj1 == obj2) return true;
        return ((obj1 != null) && (obj2 != null) && obj1.equalsIgnoreCase(obj2));
    }

    /**
     * check null or empty
     * Su dung ma nguon cua thu vien StringUtils trong apache common lang
     *
     * @param cs String
     * @return boolean
     */
    public static boolean isNullOrEmpty(CharSequence cs) {
        int strLen;
        if (cs == null || (strLen = cs.length()) == 0) {
            return true;
        }
        for (int i = 0; i < strLen; i++) {
            if (!Character.isWhitespace(cs.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    public static boolean isNullOrEmpty(final Collection<?> collection) {
        return collection == null || collection.isEmpty();
    }

    public static boolean isNullOrEmpty(final Object[] collection) {
        return collection == null || collection.length == 0;
    }

    public static boolean isNullOrEmpty(final Map<?, ?> map) {
        return map == null || map.isEmpty();
    }

    /**
     * Tra ve doi tuong default neu object la null, neu khong thi tra object
     *
     * @param object
     * @param defaultValue
     * @param <T>
     * @return
     */
    public static <T> T defaultIfNull(final T object, final T defaultValue) {
        return object != null ? object : defaultValue;
    }

    /**
     * Tra ve doi tuong default neu object la null, neu khong thi tra object
     *
     * @param object
     * @param defaultValueSupplier
     * @param <T>
     * @return
     */
    public static <T> T defaultIfNull(final T object, final Supplier<T> defaultValueSupplier) {
        return object != null ? object : defaultValueSupplier.get();
    }

    /**
     * Ham nay mac du nhan tham so truyen vao la object nhung gan nhu chi hoat dong cho doi tuong la string
     * Chuyen sang dung isNullOrEmpty thay the
     *
     * @param obj1
     * @return
     */
    @Deprecated
    public static boolean isStringNullOrEmpty(Object obj1) {
        return obj1 == null || "".equals(obj1.toString().trim());
    }

    /**
     * @param obj1 Object
     * @return BigDecimal
     */
    public static BigDecimal safeToBigDecimal(Object obj1) {
        if (obj1 == null) {
            return BigDecimal.ZERO;
        }
        try {
            return new BigDecimal(obj1.toString());
        } catch (final NumberFormatException nfe) {
            return BigDecimal.ZERO;
        }
    }

    public static BigInteger safeToBigInteger(Object obj1, BigInteger defaultValue) {
        if (obj1 == null) {
            return defaultValue;
        }
        try {
            return new BigInteger(obj1.toString());
        } catch (final NumberFormatException nfe) {
            return defaultValue;
        }
    }

    public static BigInteger safeToBigInteger(Object obj1) {
        return safeToBigInteger(obj1, BigInteger.ZERO);
    }

    public static Long add(Long number1, Long number2, Long... numbers) {
        List<Long> realNumbers = Lists.newArrayList(number1, number2);
        if (!DataUtils.isNullOrEmpty(numbers)) {
            Collections.addAll(realNumbers, numbers);
        }
        return realNumbers.stream()
                .mapToLong(DataUtils::safeToLong)
                .sum();
    }

    /**
     * add
     *
     * @param obj1 BigDecimal
     * @param obj2 BigDecimal
     * @return BigDecimal
     */
    public static BigInteger add(BigInteger obj1, BigInteger obj2) {
        if (obj1 == null) {
            return obj2;
        } else if (obj2 == null) {
            return obj1;
        }

        return obj1.add(obj2);
    }

    public static Character safeToCharacter(Object value) {
        return safeToCharacter(value, '0');
    }

    public static Character safeToCharacter(Object value, Character defaulValue) {
        if (value == null) return defaulValue;
        return String.valueOf(value).charAt(0);
    }

    public static Collection<Long> objLstToLongLst(List<Object> list) {
        Collection<Long> result = new ArrayList<>();
        if (!list.isEmpty()) {
            result.addAll(list.stream().map(DataUtils::safeToLong).collect(Collectors.toList()));
        }
        return result;
    }

    public static Collection<Short> objLstToShortLst(List<Object> list) {
        Collection<Short> result = new ArrayList<>();
        if (!list.isEmpty()) {
            result.addAll(list.stream().map(DataUtils::safeToShort).collect(Collectors.toList()));
        }
        return result;
    }

    public static Collection<BigDecimal> objLstToBigDecimalLst(List<Object> list) {
        Collection<BigDecimal> result = new ArrayList<>();
        if (!list.isEmpty()) {
            result.addAll(list.stream().map(DataUtils::safeToBigDecimal).collect(Collectors.toList()));
        }
        return result;
    }

    public static Collection<Double> objLstToDoubleLst(List<Object> list) {
        Collection<Double> result = new ArrayList<>();
        if (!list.isEmpty()) {
            result.addAll(list.stream().map(DataUtils::safeToDouble).collect(Collectors.toList()));
        }
        return result;
    }

    public static Collection<Character> objLstToCharLst(List<Object> list) {
        Collection<Character> result = new ArrayList<>();
        if (!list.isEmpty()) {
            result.addAll(list.stream().map(item -> item.toString().charAt(0)).collect(Collectors.toList()));
        }

        return result;
    }

    public static Collection<String> objLstToStringLst(List<Object> list) {
        Collection<String> result = new ArrayList<>();
        if (!list.isEmpty()) {
            result.addAll(list.stream().map(DataUtils::safeToString).collect(Collectors.toList()));
        }

        return result;
    }

    /**
     * Khong dung ham nay nua ma chuyen sang check thang == null
     *
     * @param obj1
     * @return
     */
    @Deprecated
    public static boolean isNullObject(Object obj1) {
        if (obj1 == null) {
            return true;
        }
        if (obj1 instanceof String) {
            return isNullOrEmpty(obj1.toString());
        }
        return false;
    }

    /**
     * Chuyen sang dung {@link BeanUtils# copyProperties}
     *
     * @param source
     * @param destClass
     * @param <T>
     * @return
     */
    @Deprecated
    public static <T> T mapToNewClass(Object source, Class<T> destClass) {
        try {
            if (source == null) {
                return null;
            }
            T dto;
            dto = destClass.getConstructor().newInstance();
            BeanUtils.copyProperties(source, dto);
            return dto;
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException |
                InvocationTargetException e) {
            log.error(e.getMessage(), e);
            return null;
        }
    }

    public static String toUpper(String input) {
        if (isNullOrEmpty(input)) {
            return input;
        }
        return input.toUpperCase();
    }

    /**
     * Validate Data theo Pattern
     *
     * @author khuongdv
     */
    public static boolean validateStringByPattern(String value, String regex) {
        if (isNullOrEmpty(regex) || isNullOrEmpty(value)) {
            return false;
        }
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(value);
        return matcher.matches();
    }

    public static String removeSpecialCharVn(String input) {
        if (input == null) {
            return "";
        }
        for (int i = 0; i < CHARLIST_UNICODE.length; i++) {
            input = input.replace(CHARLIST_UNICODE[i], CHARLIST_UF8[i]);
        }
        return input;
    }

    /**
     * Bien cac ki tu dac biet ve dang ascii
     *
     * @param input
     * @return
     */
    public static String convertCharacter(String input) {
        if (input == null) {
            return "";
        }
        String[] aList = {"à", "á", "ả", "ã", "ạ", "â", "ầ", "ấ", "ẩ", "ẫ", "ậ", "ă", "ằ", "ắ", "ẳ", "ẵ", "ặ"};
        String[] eList = {"è", "é", "ẻ", "ẽ", "ẹ", "ê", "ề", "ế", "ể", "ễ", "ệ"};
        String[] iList = {"ì", "í", "ỉ", "ĩ", "ị"};
        String[] oList = {"ò", "ó", "ỏ", "õ", "ọ", "ô", "ồ", "ố", "ổ", "ỗ", "ộ", "ơ", "ờ", "ớ", "ở", "ỡ", "ợ"};
        String[] uList = {"ù", "ú", "ủ", "ũ", "ụ", "ư", "ừ", "ứ", "ử", "ữ", "ự"};
        String[] yList = {"ý", "ỳ", "ỷ", "ỹ", "ỵ"};
        String[] AList = {"À", "Á", "Ả", "Ã", "Ạ", "Â", "Ầ", "Ấ", "Ẩ", "Ẫ", "Ậ", "Ă", "Ằ", "Ắ", "Ẳ", "Ẵ", "Ặ"};
        String[] EList = {"È", "É", "Ẻ", "Ẽ", "Ẹ", "Ê", "Ề", "Ế", "Ể", "Ễ", "Ệ"};
        String[] IList = {"Ì", "Í", "Ỉ", "Ĩ", "Ị"};
        String[] OList = {"Ò", "Ó", "Ỏ", "Õ", "Ọ", "Ô", "Ồ", "Ố", "Ổ", "Ỗ", "Ộ", "Ơ", "Ờ", "Ớ", "Ở", "Ỡ", "Ợ"};
        String[] UList = {"Ù", "Ú", "Ủ", "Ũ", "Ụ", "Ư", "Ừ", "Ứ", "Ử", "Ữ", "Ự"};
        String[] YList = {"Ỳ", "Ý", "Ỷ", "Ỹ", "Ỵ"};
        for (String s : aList) {
            input = input.replace(s, "a");
        }
        for (String s : AList) {
            input = input.replace(s, "A");
        }
        for (String s : eList) {
            input = input.replace(s, "e");
        }
        for (String s : EList) {
            input = input.replace(s, "E");
        }
        for (String s : iList) {
            input = input.replace(s, "i");
        }
        for (String s : IList) {
            input = input.replace(s, "I");
        }
        for (String s : oList) {
            input = input.replace(s, "o");
        }
        for (String s : OList) {
            input = input.replace(s, "O");
        }
        for (String s : uList) {
            input = input.replace(s, "u");
        }
        for (String s : UList) {
            input = input.replace(s, "U");
        }
        for (String s : yList) {
            input = input.replace(s, "y");
        }
        for (String s : YList) {
            input = input.replace(s, "Y");
        }
        input = input.replace("đ", "d");
        input = input.replace("Đ", "D");
        return input;
    }

    public static Map<String, String> convertStringToMap(String temp, String regex, String regexToMap) {
        if (isNullOrEmpty(temp)) {
            return null;
        }
        String[] q = temp.split(regex);
        Map<String, String> lstParam = new HashMap<>();
        for (String a : q) {
            String key = a.substring(0, !a.contains(regexToMap) ? 1 : a.indexOf(regexToMap));
            String value = a.substring(a.indexOf(regexToMap) + 1);
            lstParam.put(key.trim(), value.trim());
        }
        return lstParam;
    }

    /*
     * toanld2 ham xu li khoang trang giua xau
     * **/
    public static String replaceSpaceSolr(String inputLocation) {
        if (inputLocation == null || inputLocation.trim().isEmpty()) {
            return "";
        }
        String[] arr = inputLocation.split(" ");
        String result = "";
        for (String s : arr) {
            if (!"".equals(s.trim())) {
                if (!"".equals(result)) {
                    result += "\\ ";
                }
                result += s.trim();
            }
        }
        return result;
    }

    public static boolean isNumber(String string) {
        return !isNullOrEmpty(string) && string.trim().matches("^\\d+$");
    }

    /**
     * Ham format isdn bo +,0,84,084 o dau trong chuoi ISDN nhap vao
     *
     * @param rawIsdn
     * @return
     */
    public static String formatIsdn(String rawIsdn) {
        if (isNullOrEmpty(rawIsdn)) return "";
        String isdn = rawIsdn.trim();
        while (isdn.startsWith("0") || isdn.startsWith("+")) {
            isdn = isdn.substring(1);
        }
        while (isdn.startsWith("84") && isdn.length() > 10) {
            isdn = isdn.substring(2);
        }
        return isdn;
    }

    public static String formatAndAdd84(String isdn) {
        if (isNullOrEmpty(isdn)) return "";
        return "84" + formatIsdn(isdn);
    }


    public static boolean isValidFraction(String str) {
        if (str != null) {
            try {
                String tmp[] = str.split("/");
                if (tmp.length == 2) {
                    if (safeToLong(tmp[0]) < safeToLong(tmp[1])) {
                        return true;
                    }
                }
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        }
        return false;

    }

    /**
     * Tim nhung phan tu co o collection a ma khong co o collection b
     *
     * @param a
     * @param b
     * @param <T>
     * @return
     */
    public static <T> List<T> subtract(Collection<T> a, Collection<T> b) {
        if (a == null) {
            return new ArrayList<>();
        }
        if (b == null) {
            return new ArrayList<>(a);
        }
        List<T> list = new ArrayList<>(a);
        list.removeAll(b);
        return list;
    }

    public static <T> List<T> intersection(Collection<T> a, Collection<T> b) {
        if (a == null || b == null) {
            return new ArrayList<>();
        }
        List<T> list = new ArrayList<>(a);
        list.retainAll(b);
        return list;
    }

    public static <T> List<T> add(Collection<T> a, Collection<T> b) {
        if (a == null && b == null) {
            return new ArrayList<>();
        }

        if (a == null) {
            return new ArrayList<>(b);
        }

        if (b == null) {
            return new ArrayList<>(a);
        }

        List<T> list = new ArrayList<>(a);
        list.addAll(b);
        return list;
    }

    public static String removeStartingZeroes(String number) {
        if (DataUtils.isNullOrEmpty(number)) {
            return "";
        }
        return CharMatcher.anyOf("0").trimLeadingFrom(number);
    }

    /**
     * Trim string
     *
     * @param str
     * @param alt: sau thay the khi str null
     * @return
     */
    public static String trim(String str, String alt) {
        if (str == null) {
            return alt;
        }
        return str.trim();
    }


    public static String formatStringDateSubAge(String date) {
        if (date == null) {
            return "";
        }
        StringBuilder str = new StringBuilder();
        str.append(date, 4, 6);
        str.append("/");
        str.append(date, 0, 4);
        return str.toString();
    }


    public static boolean checkNotSpecialCharacter(String str) {
        return str.matches("^[A-Za-z0-9_]+");
    }

    public static Long safeAbs(Long number) {
        return safeAbs(number, 0L);

    }

    public static Long safeAbs(Long number, Long defaultValue) {
        if (number == null) {
            if (defaultValue == null) {
                return 0L;
            }
            return defaultValue < 0 ? -defaultValue : defaultValue;
        }

        return number < 0 ? -number : number;
    }


    public static String firstNonEmpty(String... strings) {
        for (String string : strings) {
            if (!isNullOrEmpty(string)) {
                return string;
            }
        }
        return "";
    }

    /**
     * ham làm tron so voi so thap phan sau dau phay
     *
     * @param value
     * @param numberAfterDot
     * @return
     */
    public static BigDecimal roundNumber(BigDecimal value, int numberAfterDot) {
        return value.setScale(numberAfterDot, BigDecimal.ROUND_HALF_UP);
    }


    public static Long convertDateToLong(String d) {
        Date date = new Date(d);
        DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        Calendar cal = Calendar.getInstance();
        if (d == null) {
            return 0L;
        }
        cal.setTime(date);
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMinimum(Calendar.DAY_OF_MONTH));
        String sdate = dateFormat.format(cal.getTime());
        return Long.valueOf(sdate);
    }

    public static String convertDateToString(Date date) {
        if (null == date) {
            return "";
        }
        DateFormat dateFormat = new SimpleDateFormat(Constants.COMMON.patternDate);
        String strDate = dateFormat.format(date);
        return strDate;
    }

    public static String convertDateToString(Date date, String format) {
        if (null == date || null == format) {
            return "";
        }
        DateFormat dateFormat = new SimpleDateFormat(format);
        String strDate = dateFormat.format(date);
        return strDate;
    }

    public static String convertInstantToString(Instant instant) {
        if (null == instant) {
            return "";
        }
        Date date = Date.from(instant);
        return convertDateToString(date, Constants.COMMON.patternDateDanCu);
    }

    public static String convertDateToStringPatter(Date date, String patternDate) {
        if (null == date) {
            return "";
        }
        DateFormat dateFormat = new SimpleDateFormat(patternDate);
        String strDate = dateFormat.format(date);
        return strDate;
    }

    public static String convertDateToStringPatterDDMMYYYY(Date date) {
        if (null == date) {
            return "";
        }
        DateFormat dateFormat = new SimpleDateFormat(Constants.COMMON.patternDateDDMMYYYY);
        String strDate = dateFormat.format(date);
        return strDate;
    }

    public static Long convertStringToLong(String month) {
        String month2 = month.replace("/", "");
        String year = month2.substring(2, 6);
        String mon = month2.substring(0, 2);
        String date = year + mon + "01";
        return Long.valueOf(date);

    }

    public static Long convertStringToFirstQuarter(String month) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("MM/yyyy");
        Date d = sdf.parse(month);
        Calendar cal = Calendar.getInstance();
        cal.setTime(d);
        cal.set(Calendar.MONTH, cal.get(Calendar.MONTH) / 3 * 3);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        LocalDate date = cal.getTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        String firstDate = String.valueOf(date);
        String customDate = firstDate.replace("-", "");
        Long firstDateOfQuarter = Long.valueOf(customDate);
        return firstDateOfQuarter;
    }

    public static Long convertStringToFirstYear(String month) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("MM/yyyy");
        Date d = sdf.parse(month);
        Calendar cal = Calendar.getInstance();
        cal.setTime(d);
        cal.set(Calendar.DAY_OF_YEAR, 1);
        LocalDate date = cal.getTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        String firstDate = String.valueOf(date);
        String customDate = firstDate.replace("-", "");
        Long firstDateOfQuarter = Long.valueOf(customDate);
        return firstDateOfQuarter;
    }

    public static String convertDateForFile(String d) {
        if (isNullOrEmpty(d))
            return "";
        Date date = new Date(d);
        DateFormat dateFormat = new SimpleDateFormat("MM/yyyy");
        String sdate = dateFormat.format(date);
        return sdate;
    }

    public static boolean compareQuarterInFileAndSystem(String d1) {
        SimpleDateFormat sdf = new SimpleDateFormat("MM/yyyy");
        boolean check = false;
        try {
            Date date1 = sdf.parse(d1);
            Date d2 = getDate();
            Date date2 = sdf.parse(sdf.format(d2));

            if ((date1.getMonth() / 3) + 1 < (date2.getMonth() / 3) + 1 && date1.getYear() <= date2.getYear()) {
                check = false;
            } else {
                check = true;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return check;

    }

    public static boolean compareFromDateAndToDate(String pstrFromDate, String pstrToDate) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        boolean check = false;
        try {
            Date date1 = sdf.parse(pstrFromDate);
            Date date2 = sdf.parse(pstrToDate);
            if (pstrToDate != null) {
                if (date1.getMonth() > date2.getMonth() && date1.getYear() >= date2.getYear()) {
                    check = false;
                } else {
                    check = true;
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return check;

    }

    public static boolean compareYearInFileAndSystem(String d1) {
        SimpleDateFormat sdf = new SimpleDateFormat("MM/yyyy");
        boolean check = false;
        try {
            Date date1 = sdf.parse(d1);
            Date d2 = getDate();
            Date date2 = sdf.parse(sdf.format(d2));

            if (date1.getYear() < date2.getYear()) {
                check = false;
            } else {
                check = true;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return check;

    }

    public static boolean compareDateAndLastSystemDate(String d1) {
        SimpleDateFormat sdf = new SimpleDateFormat("MM/yyyy");
        boolean check = false;
        try {
            Date date1 = sdf.parse(d1);
            Instant instant = Instant.ofEpochMilli(date1.getTime());
            LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
            LocalDate date = localDateTime.toLocalDate();
            LocalDate date2 = LocalDate.now().minusMonths(1);

            if (date.getMonth().compareTo(date2.getMonth()) >= 0) {
                check = true;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return check;

    }

    public static Long convertToPrdIdFirstDay(Long d) {
        String sdate;
        if (DataUtils.isNullOrZero(d)) {
            return 0L;
        } else {
            Date date = new Date(d);
            DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
            Calendar cal = Calendar.getInstance();
            if (DataUtils.isNullOrZero(d)) {
                return 0L;
            }
            cal.setTime(date);
            cal.set(Calendar.DAY_OF_MONTH, cal.getActualMinimum(Calendar.DAY_OF_MONTH));
            sdate = dateFormat.format(cal.getTime());
        }
        return Long.valueOf(sdate);
    }

    public static Date convertStringToDate(String s) {
        try {
            if (s != null) {
                Date date = new SimpleDateFormat(Constants.COMMON.patternDate).parse(s);
                return date;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Date convertStringToDate(String s, String patternDate) {
        try {
            if (s != null) {
                Date date = new SimpleDateFormat(patternDate).parse(s);
                return date;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Date getDateTimeNow() throws Exception {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = getDate();
        String local = df.format(date);
        Date datenow = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(local);
        return datenow;
    }

    public static String convertFileSize(Double size_bytes, String type) {
        double size_kb = 0;
        double size_mb = 0;
        double size_gb = 0;
        if (size_bytes > 0) {
            size_kb = size_bytes / 1024;
            size_mb = size_kb / 1024;
            size_gb = size_mb / 1024;
        }

        if ("KB".equals(type)) {
            return (Math.floor(size_kb * 100) / 100) + " KB";
        }
        if ("MB".equals(type)) {
            return (Math.floor(size_mb * 100) / 100) + " GB";
        }
        if ("GB".equals(type)) {
            return (Math.floor(size_gb * 100) / 100) + " GB";
        }
        return "0 " + type;
    }

    public static LocalDate convertToLocalDate(Date dateToConvert) {
        return dateToConvert.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
    }

    /**
     * convert blob to string
     *
     * @param blob
     * @return
     */
    public static String blobToBase64(Blob blob) {
        try {
            if (null == blob) {
                return "";
            }
            int blobLength = (int) blob.length();
            byte[] blobAsBytes = blob.getBytes(1, blobLength);

            blob.free();
            return Base64.getEncoder().encodeToString(blobAsBytes);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return "";
        }
    }

    public static byte[] blobToBytes(Object object) {
        try {
            if (object == null) {
                return null;
            }
            Blob blob = (Blob) object;
            int blobLength = (int) blob.length();
            byte[] blobAsBytes = blob.getBytes(1, blobLength);

            blob.free();
            return blobAsBytes;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return null;
        }
    }

    public static BufferedImage convertByteArrayToBufferedImage(byte[] imageInByte) throws IOException {
        if (imageInByte == null)
            return null;
        InputStream in = new ByteArrayInputStream(imageInByte);
        return ImageIO.read(in);

    }

    public static String convertLocalDateToString(LocalDate localDate) {
        String formattedDate = localDate.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
        return formattedDate;
    }

    public static String convertLocalDateToString(LocalDate localDate, String pattern) {
        String formattedDate = localDate.format(DateTimeFormatter.ofPattern(pattern));
        return formattedDate;
    }

    public static String removeAccent(String string) {
        if (StringUtils.isEmpty(string)) {
            return null;
        }
        String temp = Normalizer.normalize(string, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        temp = pattern.matcher(temp).replaceAll("");
        return temp.replaceAll("đ", "d").replaceAll("Đ", "D");
    }

    public static <Long> List<Long> removeDuplicates(List<Long> array) {
        // convert input array to populated list

        // convert list to populated set
        HashSet<Long> set = new HashSet<Long>();
        set.addAll(array);

        // convert set to array & return,
        // use cast because you can't create generic arrays
        return new ArrayList<Long>(set);
    }

    public static String convertStringToString(String stringDate) {
        Date date = DataUtils.convertStringToDate(stringDate);
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        String calendarDate = cal.get(Calendar.DATE) < 10 ? "0" + cal.get(Calendar.DATE) : cal.get(Calendar.DATE) + "";
        String calendarMonth = (cal.get(Calendar.MONTH) + 1) < 10 ? "0" + (cal.get(Calendar.MONTH) + 1) : (cal.get(Calendar.MONTH) + 1) + "";
        String formatedDate = calendarDate + "/" + calendarMonth + "/" + cal.get(Calendar.YEAR);
        return formatedDate;
    }

    public static String encodeToString(BufferedImage image, String type) {
        String imageString = null;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();

        try {
            ImageIO.write(image, type, bos);
            byte[] imageBytes = bos.toByteArray();

            Base64.Encoder encoder = Base64.getEncoder();
            imageString = encoder.encodeToString(imageBytes);

            bos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return imageString;
    }

    public static <E> E getData(ObjectMapper objectMapper, Object object, Class<E> clazz) throws IOException {
        if (object instanceof String) {
            return objectMapper.readValue(object.toString(), clazz);
        }
        return objectMapper.readValue(objectMapper.writeValueAsBytes(object), clazz);
    }

    public static String convertBienXoSe(String bienSoXe) {
        if (isNullOrEmpty(bienSoXe)) return null;
        bienSoXe = stripAccents(bienSoXe);
        bienSoXe = bienSoXe.replaceAll("[^\\w\\s]", "");
        bienSoXe = bienSoXe.replace(" ", "");
        return bienSoXe.toUpperCase();
    }

    public static String getUlrImage(String folder, String fileName) {
        return String.format("/image/%s/%s", folder, fileName);
    }

    public static String genBatchIdFromDate() {
        Date date = Calendar.getInstance().getTime();
        DateFormat dateFormat = new SimpleDateFormat("yyyymmddhhmmss");
        return RandomUtil.generateResetKey().concat(dateFormat.format(date));
    }

    public static int getPage(Pageable pageable) {
        if (pageable.getPageNumber() <= 0)
            return 0;
        else return (pageable.getPageNumber() - 1) * pageable.getPageSize();
    }

    public static String objectToJsonNotNull(Object object) {
        Gson gson = new Gson();
        return gson.toJson(object);
    }

    public static Date addHours(Date date, int hours) {
        if (date == null) {
            return null;
        }
        Calendar cal = Calendar.getInstance(); // creates calendar
        cal.setTime(date);               // sets calendar time/date
        cal.add(Calendar.HOUR_OF_DAY, hours);      // adds one hour
        return cal.getTime();
    }

    public static LocalDateTime addHours(LocalDateTime date, int hours) {
        if (date == null) {
            return null;
        }
        return date.plusHours(hours);
    }

    public static Date convertValidDate(String dateStr) {
        if (isNullOrEmpty(dateStr) || dateStr.length() != 8) {
            return null;
        }
        DateValidator validator = new DateValidatorUsingDateFormat(Constants.COMMON.patternSqlDateDC);
        if (validator.isValid(dateStr)) {
            return convertStringToDate(dateStr, Constants.COMMON.patternSqlDateDC);
        }
        return null;
    }

    public static boolean isValidDate(String dateStr) {
        if (isNullOrEmpty(dateStr) || dateStr.length() != 8) {
            return false;
        }
        DateValidator validator = new DateValidatorUsingDateFormat(Constants.COMMON.patternSqlDateDC);
        return validator.isValid(dateStr);
    }

    public static boolean isValidDateGplx(String dateStr) {
        if (safeEqual(dateStr, "31-12-9999") || safeEqual(dateStr, "Không thời hạn")) {
            return true;
        }
        DateValidator validator = new DateValidatorUsingDateFormat(Constants.COMMON.patternDate);
        return validator.isValid(dateStr);
    }

    public static List<String> lstLoaiYeuCau() {
        List<String> loaiYeuCauIgnoreMap = new ArrayList<>();
        loaiYeuCauIgnoreMap.add("2T");
        loaiYeuCauIgnoreMap.add("3T");
        loaiYeuCauIgnoreMap.add("16T");
        loaiYeuCauIgnoreMap.add("17T");
        loaiYeuCauIgnoreMap.add("0T"); // các mã có loại yc 0T sẽ có json là map với cá nhân
        return loaiYeuCauIgnoreMap;
    }


    //ls-kcb
    public static com.datastax.driver.core.LocalDate parseDate(Long lDate) {
        if (lDate != null) {
            String sDate = "" + lDate;
            Integer year = Integer.valueOf(sDate.substring(0, 4));
            Integer month = Integer.valueOf(sDate.substring(4, 6));
            Integer day = Integer.valueOf(sDate.substring(6, 8));
//      Integer hour = Integer.valueOf(sDate.substring(8,10));
//      Integer minutes = Integer.valueOf(sDate.substring(10,12));
            com.datastax.driver.core.LocalDate localDate = com.datastax.driver.core.LocalDate.fromYearMonthDay(year, month, day);
            return localDate;
        }
        return null;
    }

    public static Date parseDateTime(Long lDate) {
        if (lDate != null) {
            String sDate = "" + lDate;
            Integer year = Integer.valueOf(sDate.substring(0, 4));
            Integer month = Integer.valueOf(sDate.substring(4, 6));
            Integer day = Integer.valueOf(sDate.substring(6, 8));
            Integer hour = Integer.valueOf(sDate.substring(8, 10));
            Integer minutes = Integer.valueOf(sDate.substring(10, 12));
            Calendar calendar = Calendar.getInstance();
            calendar.set(year, month, day, hour, minutes);
            return calendar.getTime();
        }
        return null;
    }

    //ls-kcb
    public static com.datastax.driver.core.LocalDate parseDate(String lDate) {
        if (!Strings.isNullOrEmpty(lDate)) {
            Integer year = Integer.valueOf(lDate.substring(0, 4));
            Integer month = Integer.valueOf(lDate.substring(4, 6));
            Integer day = Integer.valueOf(lDate.substring(6, 8));
//      Integer hour = Integer.valueOf(sDate.substring(8,10));
//      Integer minutes = Integer.valueOf(sDate.substring(10,12));
            com.datastax.driver.core.LocalDate localDate = com.datastax.driver.core.LocalDate.fromYearMonthDay(year, month, day);
            return localDate;
        }
        return null;
    }

    public static Date parseDateTime(String lDate) {
        if (!Strings.isNullOrEmpty(lDate)) {
            Integer year = Integer.valueOf(lDate.substring(0, 4));
            Integer month = Integer.valueOf(lDate.substring(4, 6));
            Integer day = Integer.valueOf(lDate.substring(6, 8));
            Integer hour = Integer.valueOf(lDate.substring(8, 10));
            Integer minutes = Integer.valueOf(lDate.substring(10, 12));
            Calendar calendar = Calendar.getInstance();
            calendar.set(year, month, day, hour, minutes);
            return calendar.getTime();
        }
        return null;
    }

    public static Date parseDateTime(String date, String pattern) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
        try {
            return dateFormat.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String formatSpaceString(String data) {
        if (data == null) return "";
        data = data.replaceAll("\t", "");
        data = data.replaceAll("\n", "");
        data = data.replaceAll(" ", "");
        return data;
    }

    public static String randomOtp(){
        StringBuilder otp = new StringBuilder();
        for (int i = 0; i < 5; i++) {
            otp.append(new Random().nextInt(9));
        }
        return otp.toString();
    }

    public static String randomPassword() {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder password = new StringBuilder();
        for (int i = 0; i < 8; i++) {
            int index = new Random().nextInt(characters.length());
            password.append(characters.charAt(index));
        }
        return password.toString();
    }

    public static List<Week> getWeeksInYear(int year) {
        List<Week> weeks = new ArrayList<>();

        // Ngày đầu tiên của năm
        LocalDate firstDayOfYear = LocalDate.of(year, 1, 1);

        // Tìm ngày thứ Hai đầu tiên của năm
        LocalDate firstMonday = firstDayOfYear.with(TemporalAdjusters.nextOrSame(DayOfWeek.MONDAY));

        // Lặp qua các tuần trong năm
        LocalDate currentMonday = firstMonday;
        while (currentMonday.getYear() == year) {
            LocalDate currentFriday = currentMonday.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY));

            weeks.add(new Week(currentMonday, currentFriday));

            // Di chuyển đến tuần tiếp theo
            currentMonday = currentMonday.plusWeeks(1);
        }

        return weeks;
    }
}
