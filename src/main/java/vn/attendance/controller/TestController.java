//package vn.attendance.controller;
//
//import com.fasterxml.jackson.core.JsonProcessingException;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.apache.commons.beanutils.BeanUtilsBean;
//import org.apache.commons.beanutils.ConvertUtilsBean;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.CrossOrigin;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//import vn.attendance.helper.ApiResponse;
//import vn.attendance.repository.UsersRepository;
//import vn.attendance.util.Constants;
//
//import java.lang.reflect.InvocationTargetException;
//import java.util.UUID;
//
//@CrossOrigin(origins = "*", maxAge = 3600)
//@RestController
//@RequestMapping("api/v1/test")
//public class TestController extends BaseController {
//    @Autowired
//    UsersRepository userRepository;
//
//    public static void main(String[] args) throws JsonProcessingException, InvocationTargetException, IllegalAccessException, NoSuchMethodException {
//        String json = "{\n" +
//                "    \"id\": 1,\n" +
//                "    \"value\": {\n" +
//                "        \"id\": 4,\n" +
//                "        \"username\": \"LongNV2\",\n" +
//                "        \"password\": \"$2a$10$Xho3HWwcJQ9215pT.SOCaO4MA8VbRtQTyr4ahDvBPisbDI5bqZSoq\",\n" +
//                "        \"ruleId\": 1,\n" +
//                "        \"createDate\": null,\n" +
//                "        \"createBy\": \"system\",\n" +
//                "        \"isActive\": null,\n" +
//                "        \"updateDate\": null,\n" +
//                "        \"updateBy\": null\n" +
//                "    }\n" +
//                "}";
//        System.out.println(new BeanUtilsBean(new ConvertUtilsBean() {
//            @Override
//            public Object convert(String value, Class clazz) {
//                if (clazz.equals(UUID.class)) {
//                    return UUID.fromString(value);
//                } else {
//                    return super.convert(value, clazz);
//                }
//            }
//        }).getProperty(new ObjectMapper().readValue(json, Object.class), "id"));
//    }
//
//    @GetMapping("hi")
//    public ResponseEntity<?> abnc() {
//        try {
//            return new ResponseEntity<>(ApiResponse.build(HttpStatus.OK.value(), Constants.COMMON.SUCCESS, userRepository.findAll()), null, HttpStatus.OK);
//        } catch (Exception ex) {
//            ex.printStackTrace();
//            return new ResponseEntity<>(ApiResponse.build(HttpStatus.NOT_FOUND.value(), ex.getMessage(), null), null, HttpStatus.OK);
//        }
//    }
//
//    @GetMapping("hi2")
//    public ResponseEntity<?> abn2c() {
//        try {
//            return new ResponseEntity<>(ApiResponse.build(HttpStatus.OK.value(), Constants.COMMON.SUCCESS, userRepository.findAll()), null, HttpStatus.OK);
//        } catch (Exception ex) {
//            ex.printStackTrace();
//            return new ResponseEntity<>(ApiResponse.build(HttpStatus.NOT_FOUND.value(), ex.getMessage(), null), null, HttpStatus.OK);
//        }
//    }
//
//
//}
