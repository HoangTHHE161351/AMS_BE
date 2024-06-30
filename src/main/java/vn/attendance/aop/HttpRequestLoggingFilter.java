//package vn.attendance.aop;
//import com.google.gson.Gson;
//import com.google.gson.JsonElement;
//import com.google.gson.JsonObject;
//import org.apache.commons.lang3.exception.ExceptionUtils;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.stereotype.Component;
//import org.springframework.util.StreamUtils;
//import vn.attendance.config.authen.JwtTokenAuthFilter;
//import vn.attendance.config.authen.TokenProvider;
//
//import javax.servlet.*;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletRequestWrapper;
//import javax.servlet.http.HttpServletResponse;
//import java.io.ByteArrayInputStream;
//import java.io.IOException;
//import java.io.InputStream;
//import java.nio.charset.StandardCharsets;
//import java.text.DecimalFormat;
//import java.text.SimpleDateFormat;
//import java.time.Duration;
//import java.time.LocalDateTime;
//import java.time.format.DateTimeFormatter;
//import java.util.Date;
//import java.util.Enumeration;
//
//@Component
//public class HttpRequestLoggingFilter implements Filter{
//    private static final Logger logger = LoggerFactory.getLogger(HttpRequestLoggingFilter.class);
//    private static final int MegaBytes = 1024 * 1024;
//    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss.SSS");
//    DecimalFormat decimalFormat = new DecimalFormat("0.00");
//
//    @Autowired
//
//    @Autowired
//    private TokenProvider tokenProvider;
//
//    @Autowired
//    private JwtTokenAuthFilter jwtAuthTokenFilter;
//
//    @Autowired
//    private BCryptPasswordEncoder bCryptPasswordEncoder;
//
//    @Override
//    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
//            throws IOException, ServletException {
//        CachedRequestHttpServletRequest cachedRequestHttpServletRequest = null;
//        try {
//            request.setAttribute("startTime", this.getNowFormated());
//            request.setAttribute("startFreeMemory", Runtime.getRuntime().freeMemory());
//            if (request.getContentType() != null && request.getContentType().equals(Constants.CONTENT_TYPE_REQUEST)) {
//                cachedRequestHttpServletRequest = new CachedRequestHttpServletRequest((HttpServletRequest) request);
//                chain.doFilter(cachedRequestHttpServletRequest, response);
//            } else {
//                chain.doFilter(request, response);
//            }
//
//        } catch (Exception e) {
//            jwtAuthTokenFilter.buildExceptionOutput((HttpServletRequest)request, (HttpServletResponse)response, HttpStatus.INTERNAL_SERVER_ERROR.name(), e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value(), HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
//            logErrorException(e, HttpStatus.INTERNAL_SERVER_ERROR.toString());
//        } finally {
//            if (request.getContentType() != null && request.getContentType().equals(Constants.CONTENT_TYPE_REQUEST)) {
//                this.logFilter(cachedRequestHttpServletRequest, cachedRequestHttpServletRequest.getCachedBody(), (HttpServletResponse)response);
//            } else {
//                cachedRequestHttpServletRequest = new CachedRequestHttpServletRequest((HttpServletRequest) request);
//                this.logFilter(cachedRequestHttpServletRequest ,cachedRequestHttpServletRequest.getCachedBody(), (HttpServletResponse)response);
//            }
//        }
//    }
//
//    private void logFilter(CachedRequestHttpServletRequest request, byte[] cachedBody, HttpServletResponse response) {
//        try {
//            JsonObject json = new JsonObject();
//            String startTime = (String) request.getAttribute("startTime");
//            String endTime = this.getNowFormated();
//            String userId = Utils.formatSpaceString(this.getUserIdFromJwt(request));
//            String requestURL = Utils.formatSpaceString(request.getRequestURL().toString());
//            String method = request.getMethod();
//            JsonObject requestParam = getRequestParam(request);
//            JsonObject requestHeader = getRequestHeader(request);
//            String requestBody = Utils.formatSpaceString(new String(cachedBody, StandardCharsets.UTF_8));
//            try {
//                JsonObject convertedObject = new Gson().fromJson(requestBody, JsonObject.class);
//                if (requestURL.toString().contains("/api/v1/user/authentication/external/login")) {
//                    convertedObject.remove("password");
//                }
////                if (convertedObject.get("password")!= null){
////                    convertedObject.addProperty("password",bCryptPasswordEncoder.encode(convertedObject.get("password").toString()));
////                }
//                json.add("requestBody", convertedObject);
//            } catch (Exception ex) {
//                json.add("requestBody", null);
//            }
//            try {
//
//                if (requestURL.toString().contains("/api/v1/user/unit/video-offline")) {
//                    json.add("requestBody", new Gson().fromJson(requestBody, JsonElement.class));
//                }else {
//                    JsonObject convertedObject = new Gson().fromJson(requestBody, JsonObject.class);
//                    if (convertedObject.get("password") != null) {
//                        convertedObject.addProperty("password", bCryptPasswordEncoder.encode(convertedObject.get("password").toString()));
//                    }
//                    json.add("requestBody", convertedObject);
//                }
//
//            } catch (Exception ex) {
//                json.add("requestBody", null);
//            }
//            try {
//                if (Utils.safeEqual(response.getStatus(), 500)) {
//                    json.add("requestBody", new Gson().fromJson(requestBody, JsonElement.class));
//                }
//            }catch (Exception ex){
//            }
//            try{
//                //get platform call API
//                String sec_ch_ua_platform = requestHeader.get("sec-ch-ua-platform") != null ? requestHeader.get("sec-ch-ua-platform").getAsString().replace("\"","") : null;
//                String sec_ch_ua_mobile = requestHeader.get("sec-ch-ua-mobile") != null ? requestHeader.get("sec-ch-ua-mobile").getAsString().replace("\"","") : null;
//                String sec_ch_ua = requestHeader.get("sec-ch-ua")!= null ? requestHeader.get("sec-ch-ua").getAsString().replace("\"","") : null;
//                json.addProperty("platform",sec_ch_ua_platform);
////                json.addProperty("mobile",!Utils.safeEqual(sec_ch_ua_mobile,"?0") ? (Utils.safeEqual(sec_ch_ua_mobile,"?1") ? "true": sec_ch_ua_mobile) : "false") ;
//                json.addProperty("browserInfo",sec_ch_ua);
//                if (Utils.isNullOrEmpty(sec_ch_ua_mobile)){
//                    //get infor by user-agent
////                    List<String> osList = new ArrayList<>(Arrays.asList("android","ios", "iphone"))
//                    String userAgent = requestHeader.get("user-agent").getAsString();
//                    if (userAgent != null) { // Regular expression to match the platform
//                        if (userAgent.toLowerCase().contains("okhttp") || userAgent.toLowerCase().contains("mobile"))
//                            json.addProperty("mobile","true") ;
//                        json.addProperty("browserInfo", "Web Browser");
//                    }
//                }else {
//                    json.addProperty("mobile", !Utils.safeEqual(sec_ch_ua_mobile, "?0") ? (Utils.safeEqual(sec_ch_ua_mobile, "?1") ? "true" : sec_ch_ua_mobile) : "false");
//                    json.addProperty("browserInfo", sec_ch_ua);
//                }
//                json.addProperty("app-version", requestHeader.get("app-version") != null ? requestHeader.get("app-version").getAsString() : null);
//                requestHeader.remove("sec-ch-ua-platform");
//                requestHeader.remove("sec-ch-ua-mobile");
//                requestHeader.remove("sec-ch-ua");
//                requestHeader.remove("sec-ch-ua");
//                requestHeader.remove("sec-fetch-dest");
//                requestHeader.remove("sec-fetch-mode");
//                requestHeader.remove("sec-fetch-site");
//            }catch (Exception e){
//            }
//            try {
//                long startFreeMemory = (Long) request.getAttribute("startFreeMemory")/MegaBytes;
//                long endFreeMemory = Runtime.getRuntime().freeMemory()/MegaBytes;
//                long maxMemory = Runtime.getRuntime().maxMemory()/MegaBytes;
//                long totalMemory = Runtime.getRuntime().totalMemory()/MegaBytes;
//                json.addProperty("JVM_StartFreeMemory", startFreeMemory + "MB");
//                json.addProperty("JVM_EndFreeMemory", endFreeMemory + "MB");
//                json.addProperty("JVM_UseMemory", (endFreeMemory-startFreeMemory) + "MB");
//                json.addProperty("JVM_MaxMemory", (maxMemory) + "MB");
//                json.addProperty("JVM_TotalMemory", (totalMemory) + "MB");
//            } catch (Exception ex) {
//                json.addProperty("JVM_StartFreeMemory", "none");
//                json.addProperty("JVM_EndFreeMemory", "none");
//                json.addProperty("JVM_UseMemory", "none");
//                json.addProperty("JVM_MaxMemory", "none");
//                json.addProperty("JVM_TotalMemory", "none");
//            }
//            json.addProperty("totalTime", getDurationExecuteAPI(startTime,endTime));
//            json.addProperty("isSlow", getDurationExecuteAPI(startTime,endTime) > 5);
//            json.addProperty("requestURL", requestURL);
//            json.addProperty("userId", userId);
//            json.addProperty("method", method);
//            json.add("requestParam", requestParam);
//            json.add("requestHeader", requestHeader);
//            if (requestURL.toString().contains("/api/v1/admin/company-management/change-block")) {
//                json.addProperty("requestBody", requestBody);
//                Boolean blockCompany = getRequestParam(request).get("status").getAsBoolean();
//                json.addProperty("companyStatus", blockCompany ? "BLOCK" : "UNBLOCK");
//            }
//            json.addProperty("statusCode", response.getStatus());
//            String message = Utils.formatSpaceString(json.toString());
//            logger.info(message);
//
//            if (requestURL.toString().contains("/api/v1/admin/robot-payment/verify")) {
//                JsonObject jsonPayment = new JsonObject();
//                LogPayment logPayment = new LogPayment();
//                logPayment.setPaymentId(method);
//                logPayment.setPaymentStatus("log-verify");
//                logPayment.setPaymentMessage(message);
//                jsonPayment.addProperty("requestURL", requestURL);
//                jsonPayment.addProperty("userId", userId);
//                jsonPayment.addProperty("method", method);
//                jsonPayment.add("requestParam", requestParam);
//                jsonPayment.addProperty("statusCode", response.getStatus());
//                logPayment.setPaymentMessage(Utils.formatSpaceString(jsonPayment.toString()));
//                logPaymentRepository.save(logPayment);
//            }
//        } catch(Exception ex) {
//            logErrorException(ex, "none");
//        }
//    }
//
//    //---- function support
//    private JsonObject getRequestParam(CachedRequestHttpServletRequest request) {
//        JsonObject requestParam = new JsonObject();
//        try {
//            Enumeration<String> enumeration = request.getParameterNames();
//            while(enumeration.hasMoreElements()){
//                String parameterName = enumeration.nextElement();
//                requestParam.addProperty(parameterName, request.getParameter(parameterName));
//            }
//            return requestParam;
//        } catch(Exception ex) {
//            logErrorException(ex, "none");
//        }
//        return requestParam;
//    }
//
//    private JsonObject getRequestHeader(CachedRequestHttpServletRequest request) {
//        JsonObject requestHeader = new JsonObject();
//        try {
//            Enumeration<String> enumerationHeader = request.getHeaderNames();
//            while(enumerationHeader.hasMoreElements()){
//                String parameterName = enumerationHeader.nextElement();
//                String value = request.getHeader(parameterName);
//                if (value != null) {
//                    requestHeader.addProperty(parameterName, value);
//                }
//            }
//            return requestHeader;
//        } catch(Exception ex) {
//            logErrorException(ex, "none");
//        }
//        return requestHeader;
//    }
//
//    private String getUserIdFromJwt(CachedRequestHttpServletRequest request) {
//        try {
//            String authHeader = request.getHeader(Constants.AUTHORIZATION_HEADER);
//            if (authHeader != null && authHeader.startsWith(Constants.JWT_TOKEN_TYPE)) {
//                String jwt = authHeader.replace(Constants.JWT_TOKEN_TYPE, "");
//                if (jwt != null && this.tokenProvider.validateJwtToken(jwt)) {
//                    String userId = String.valueOf(this.tokenProvider.getClaimInfo(jwt, "userId"));
//                    return userId;
//                }
//            }
//        } catch(Exception ex) {
//            logger.error(ex.getMessage());
//        }
//        return "";
//    }
//
//
//
//    private class CachedRequestHttpServletRequest extends HttpServletRequestWrapper {
//
//        private byte[] cachedBody;
//
//        public CachedRequestHttpServletRequest(HttpServletRequest request) throws IOException {
//            super(request);
//            try {
//                this.cachedBody = StreamUtils.copyToByteArray(request.getInputStream());
//            } catch (Exception e) {
//                logErrorException(e, "none");
//            }
//        }
//
//        public byte[] getCachedBody() {
//            return this.cachedBody;
//        }
//
//        @Override
//        public ServletInputStream getInputStream() {
//            return new CachedRequestServletInputStream(this.cachedBody);
//        }
//    }
//
//    private class CachedRequestServletInputStream extends ServletInputStream {
//
//        private InputStream cachedBodyInputStream;
//
//        public CachedRequestServletInputStream(byte[] cachedBody) {
//            this.cachedBodyInputStream = new ByteArrayInputStream(cachedBody);
//        }
//
//        @Override
//        public boolean isFinished() {
//            try {
//                return cachedBodyInputStream.available() == 0;
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            return false;
//        }
//
//        @Override
//        public boolean isReady() {
//            return true;
//        }
//
//        @Override
//        public void setReadListener(ReadListener readListener) {
//            throw new UnsupportedOperationException();
//        }
//
//        @Override
//        public int read() throws IOException {
//            return cachedBodyInputStream.read();
//        }
//    }
//
//    /**
//     * log error exception
//     * @param ex
//     * @param httpStatus
//     */
//    private void logErrorException(Exception ex, String httpStatus) {
//        JsonObject json = new JsonObject();
//        String stacktrace = ExceptionUtils.getStackTrace(ex);
//        stacktrace = Utils.formatSpaceString(stacktrace);
//        json.addProperty("HttpStatus", httpStatus);
//        json.addProperty("stacktrace", stacktrace);
//        logger.error(Utils.formatSpaceString(json.toString()));
//    }
//
//    /**
//     * get now with formated yyy-MM-dd hh:mm:ss.SSS
//     * @return
//     */
//    private String getNowFormated() {
//        try {
//            String patternDate = "yyy-MM-dd_HH:mm:ss.SSS";
//            return new SimpleDateFormat(patternDate).format(new Date());
//        } catch(Exception ex) {}
//        return null;
//    }
//
//    private long getDurationExecuteAPI(String startTime, String endTime) {
//        try {
//            String patternDate = "yyy-MM-dd_HH:mm:ss.SSS";
//            if(Utils.isNullOrEmpty(startTime)  || Utils.isNullOrEmpty(endTime)){
//                logger.error("Error calculateTime api [Start Time : {} , End Time : {}]", startTime, endTime);
//                return 0L;
//            } else {
//                startTime.replace(" ", "");
//                endTime.replace(" ","");
//                LocalDateTime start = LocalDateTime.parse(startTime, DateTimeFormatter.ofPattern(patternDate));
//                LocalDateTime end = LocalDateTime.parse(endTime, DateTimeFormatter.ofPattern(patternDate));
//                return Math.abs(Duration.between(start,end).getSeconds());
//            }
//        }catch (Exception ex){
//            logger.error("[Mess : {}][Start Time : {} , End Time : {}]",ex.getMessage(),startTime,endTime);
//            ex.printStackTrace();
//            return 0L;
//        }
//    }
//}