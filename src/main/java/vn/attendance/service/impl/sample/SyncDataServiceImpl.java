package vn.attendance.service.impl.sample;//package vn.sphinx.hysmart.logistic.service.impl.dancu;
//
//import com.fasterxml.jackson.core.type.TypeReference;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.xml.sax.InputSource;
//import org.apache.commons.codec.digest.DigestUtils;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.http.*;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//import org.springframework.web.client.RestTemplate;
//import org.w3c.dom.CharacterData;
//import org.w3c.dom.*;
//import vn.sphinx.hysmart.logistic.model.LogSynchronizedCitizenData;
//import vn.sphinx.hysmart.logistic.repository.LogSyncDataRepository;
//import vn.sphinx.hysmart.logistic.repository.NguoiTiemRepository;
//import vn.sphinx.hysmart.logistic.repository.NidJobParamRepository;
//import vn.sphinx.hysmart.logistic.service.dancu.SyncDataService;
//import vn.sphinx.hysmart.logistic.service.dto.responses.InjectionResponse;
//import vn.sphinx.hysmart.logistic.service.dto.responses.InjectionSyncResponse;
//import vn.sphinx.hysmart.logistic.service.impl.DanCuServiceImpl;
//import vn.sphinx.hysmart.logistic.util.Constants;
//import vn.sphinx.hysmart.logistic.util.DataUtil;
//
//import javax.xml.parsers.DocumentBuilder;
//import javax.xml.parsers.DocumentBuilderFactory;
//import java.io.StringReader;
//import java.sql.Timestamp;
//import java.util.*;
//import java.util.stream.Collectors;
//
//@Service
//public class SyncDataServiceImpl implements SyncDataService {
//
//
//    private static final Logger logger = LoggerFactory.getLogger(DanCuServiceImpl.class);
//
//    @Value("${syncDataCitizen.endpoint}")
//    private String endpointSync;
//
//    @Value("${syncDataCitizen.baseUrl}")
//    private String baseUrlCitizen;
//
//    @Value("${syncDataCitizen.username}")
//    private String userName;
//
//    @Value("${syncDataCitizen.password}")
//    private String password;
//
//    @Autowired
//    private TiemChungRepository tiemChungRepository;
//
//    @Autowired
//    private LogSyncDataRepository logSyncDataJpaRepository;
//
//    @Autowired
//    private NidJobParamRepository nidJobParamRepository;
//
//    @Autowired
//    private NguoiTiemRepository nguoiTiemRepository;
//
//    @Transactional
//    @Override
//    public Integer callApiSyncData(Date fromDate, Date toDate , String dataType, Long pageNumber, Long pageSize) throws Exception{
//
//        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
//        Long time = timestamp.getTime();
//        String token = DigestUtils.sha256Hex(endpointSync + userName + timestamp.getTime() + password);
//        String authorization = Base64.getEncoder().encodeToString((userName + ":" + token).getBytes());
//        Integer isExistData = 0;
//        try {
//            String sql = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:dan=\"http://dancuquocgia.bca\">\n" +
//                    "    <soap:Header xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\"></soap:Header>\n" +
//                    "    <soapenv:Body>\n" +
//                    "        <dan:TraCuuThongTinCongDan xmlns:dan=\"http://dancuquocgia.bca\">\n" +
//                    "            <dan:MaYeuCau>" +DataUtil.genBatchIdFromDate() + "</dan:MaYeuCau>\n" +
//                    "            <dan:TuNgay>" +fromDate.getTime() + "</dan:TuNgay>\n" +
//                    "            <dan:DenNgay>"+ toDate.getTime() +"</dan:DenNgay>\n" +
//                    "            <dan:Loai>"+ dataType +"</dan:Loai>\n"+
//                    "            <dan:Trang>"+ pageNumber +"</dan:Trang>\n"+
//                    "            <dan:SoBanGhi>"+ pageSize +"</dan:SoBanGhi>\n"+
//                    "        </dan:TraCuuThongTinCongDan>\n" +
//                    "    </soapenv:Body>\n" +
//                    "</soapenv:Envelope>\n";
//            RestTemplate restTemplate = new RestTemplate();
//            HttpHeaders headers = new HttpHeaders();
//            headers.setContentType(MediaType.APPLICATION_XML);
//            headers.add("Timestamp", String.valueOf(time));
//            headers.add("Authorization", "Basic " + authorization);
//            headers.add("ESB_SOURCE_TYPE", userName);
//            headers.add("ESB_SOURCE_USERNAME", userName);
//
//            HttpEntity<String> entity = new HttpEntity<String>(sql, headers);
//            ResponseEntity<String> resTemp = restTemplate.exchange(baseUrlCitizen+endpointSync, HttpMethod.POST, entity, String.class);
//            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
//            dbf.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
//            DocumentBuilder db = dbf.newDocumentBuilder();
//            InputSource is = new InputSource();
//
//            is.setCharacterStream(new StringReader(resTemp.getBody()));
//            Document doc = db.parse(is);
//            NodeList danhSachDanCu = doc.getElementsByTagName("ns1:CongDan");
//            if (danhSachDanCu.getLength() > 0) {
//                for (int i = 0; i < danhSachDanCu.getLength(); i++) {
//                    Element elementCongDan = (Element) danhSachDanCu.item(i);
//                    Element bodyEl = (Element)  elementCongDan.getElementsByTagName("ns1:Body").item(0);
//                    Element soDinhDanh = (Element)  elementCongDan.getElementsByTagName("ns1:SoDinhDanh").item(0);
//                    ObjectMapper objectMapper = new ObjectMapper();
//                    String jsonValue = getCharacterDataFromElement(bodyEl);
//                    jsonValue = jsonValue.replace("\\","")
//                            .replace("\"[","[")
//                            .replace("]\"","]");
//                    List<InjectionSyncResponse> bodyResnponse = objectMapper.readValue(jsonValue, new TypeReference<List<InjectionSyncResponse>>(){});
//                    if(!DataUtil.isNullOrEmpty(bodyResnponse)){
//                        try{
//                            InjectionSyncResponse response = bodyResnponse.get(0);
//                            //ghi dữ liệu vào oracle
////                            TiemChung save = saveSyncData(response,dataType,fromDate,toDate,getCharacterDataFromElement(bodyEl), getCharacterDataFromElement(soDinhDanh));
//                              NguoiTiem save = saveNguoiTiem(response,dataType,fromDate,toDate,getCharacterDataFromElement(bodyEl), getCharacterDataFromElement(soDinhDanh));
//                            isExistData = 1;
//                        }catch (Exception ex){
//                            logger.error(ex.getMessage(),ex);
//                        }
//                    }
//                }
//            } else {
//                isExistData = 2;
//                System.out.printf("khong tim thay du lieu");
//                //ghi log check
//                LogSynchronizedCitizenData citizenData = new LogSynchronizedCitizenData();
//                citizenData.setDataType(dataType);
//                citizenData.setPkActionType("CHECK");
//                citizenData.setSyncDate(new Date());
//                citizenData.setRequestDateFrom(fromDate);
//                citizenData.setRequestDateTo(toDate);
//                citizenData.setRequestContent(resTemp.getBody());
//                citizenData.setErroCode(String.valueOf(pageNumber));
//                logSyncDataJpaRepository.save(citizenData);
//            }
//        } catch (Exception e) {
//            logger.error(e.getMessage(),e);
////            throw e;
//        }
//        return isExistData;
//    }
//
//    @Transactional
//    public void updateParamByCodeAndType(String paramName, String valueParam, Integer type){
//        try{
//            nidJobParamRepository.updateValueByParamAndType(paramName,type,valueParam);
//        }catch (Exception e)
//        {
//            e.printStackTrace();
//        }
//    }
//
//    @Override
//    @Transactional
//    public NguoiTiem saveNguoiTiem(InjectionSyncResponse syncDataResponse, String dataType, Date fromDate, Date toDate, String jsonResponse, String soDinhDanh) {
//        if(syncDataResponse != null && !DataUtil.isNullOrEmpty(syncDataResponse.getInjectionInfo())){
//            NguoiTiem nguoiTiemRequet = new NguoiTiem();
//            List<MuiTiem> muiTiemList = new ArrayList<>();
//            String erroCode ="";
//            boolean isUpdate = false;
//
//            String fullnameKD = DataUtil.stripAccents(syncDataResponse.getFullName()).toUpperCase().trim();
//
//            erroCode = validateNguoiTiem(syncDataResponse);
//            if(DataUtil.isNullOrEmpty(soDinhDanh)){
//                erroCode= "4";// khong ton tai so dinh danh
//            }
//            Date ngaySinh = DataUtil.convertStringToDate(syncDataResponse.getBirthDate(),Constants.COMMON.patternSqlDateDC);
//            if(DataUtil.isNullOrEmpty(erroCode)){
//                NguoiTiem checkExit =  nguoiTiemRepository.getExistTiemChung(fullnameKD,ngaySinh,soDinhDanh).orElse(null);
//                if(checkExit!=null){
//                    isUpdate = true;
//                    nguoiTiemRequet = checkExit;
//                }
//                nguoiTiemRequet.setNgaySinh(ngaySinh);
//                nguoiTiemRequet.setTenKhaiSinh(syncDataResponse.getFullName());
//                nguoiTiemRequet.setTenKhaiSinhKd(fullnameKD);
//                nguoiTiemRequet.setSoCmndCccd(soDinhDanh);
//                nguoiTiemRequet.setIndentifyNumber(syncDataResponse.getIdentifyNumber());
//                nguoiTiemRequet.setSoDienThoai(syncDataResponse.getPhoneNumber());
//                nguoiTiemRequet.setNguonDuLieu("DC");
//
//                nguoiTiemRequet.setThuongTruChiTiet(syncDataResponse.getHomeAddress());
//                nguoiTiemRequet.setThuongTruXa(syncDataResponse.getHomeVillage());
//                nguoiTiemRequet.setThuongHuyen(syncDataResponse.getHomeProvince());
//                nguoiTiemRequet.setThuongTruTinh(syncDataResponse.getHomeCity());
//                if(isUpdate){
//                    nguoiTiemRequet.setNgaySua(new Date());
//                    nguoiTiemRequet.setNguoiSua("SyncVacxin");
//                    //lay ra danh sach mui tiem moi
//                    List<InjectionResponse> cloneInjectionResponse = DataUtil.deepCloneObject(syncDataResponse.getInjectionInfo());
//                    for (MuiTiem muiTiemOld :nguoiTiemRequet.getListMuiTiem()){
//                        cloneInjectionResponse.removeIf(x->DataUtil.safeEqual(x.getInjectionOrder(),muiTiemOld.getSoMuiTiem()));
//                    }
//                    if(!DataUtil.isNullOrEmpty(cloneInjectionResponse)){
//                        for(InjectionResponse newMuiTiem:cloneInjectionResponse){
//
//                            MuiTiem muiTiem = new MuiTiem();
//                            erroCode = vaildateDataTiem(newMuiTiem);
//                            if(!DataUtil.isNullOrEmpty(erroCode)){
//                                break;
//                            }
//                            if(!DataUtil.isNullOrEmpty(newMuiTiem.getInjectionOrder())){
//                                muiTiem.setNguoiTiemId(nguoiTiemRequet.getId());
//                                muiTiem.setSoMuiTiem(newMuiTiem.getInjectionOrder());
//                                muiTiem.setTenVaccine(newMuiTiem.getVaxcinName());
//                                muiTiem.setLoVaccine(newMuiTiem.getPackageNumberVaxcin());
//                                muiTiem.setDonViTiem(newMuiTiem.getAddressInjection());
//                                muiTiem.setNgayTiem(DataUtil.convertStringToDate(newMuiTiem.getInjectionDate(), Constants.COMMON.patternDateDanCu));
//                                muiTiem.setNgayTao(new Date());
//                                muiTiem.setNguoiNhap("SyncVacxin");
//                                muiTiemList.add(muiTiem);
//                            }
//                        }
//
//                    }
//
//                    for (MuiTiem muiTiemOld :nguoiTiemRequet.getListMuiTiem()){
//                        for (InjectionResponse objects : syncDataResponse.getInjectionInfo()) {
//                            if(DataUtil.safeEqual(objects.getInjectionOrder(),muiTiemOld.getSoMuiTiem())){
//                                //update mui tiem
//                                erroCode = vaildateDataTiem(objects);
//                                if(!DataUtil.isNullOrEmpty(erroCode)){
//                                    break;
//                                }
//                                muiTiemOld.setSoMuiTiem(objects.getInjectionOrder());
//                                muiTiemOld.setTenVaccine(objects.getVaxcinName());
//                                muiTiemOld.setLoVaccine(objects.getPackageNumberVaxcin());
//                                muiTiemOld.setDonViTiem(objects.getAddressInjection());
//                                muiTiemOld.setNgayTiem(DataUtil.convertStringToDate(objects.getInjectionDate(), Constants.COMMON.patternDateDanCu));
//                                muiTiemOld.setNgaySua(new Date());
//                                muiTiemOld.setNguoiSua("SyncVacxin");
//                            }
//                        }
//                    }
//                    if(DataUtil.isNullOrEmpty(erroCode)) {
//                        nguoiTiemRequet.getListMuiTiem().addAll(muiTiemList);
//                    }
//                }else {
//                    nguoiTiemRequet.setNgayTao(new Date());
//                    nguoiTiemRequet.setNguoiNhap("SyncVacxin");
//                    for (InjectionResponse objects : syncDataResponse.getInjectionInfo()) {
//                        MuiTiem muiTiem = new MuiTiem();
//                        erroCode = vaildateDataTiem(objects);
//                        if(!DataUtil.isNullOrEmpty(erroCode)){
//                            break;
//                        }
//                        if(!DataUtil.isNullOrEmpty(objects.getInjectionOrder())){
//                            muiTiem.setSoMuiTiem(objects.getInjectionOrder());
//                            muiTiem.setTenVaccine(objects.getVaxcinName());
//                            muiTiem.setLoVaccine(objects.getPackageNumberVaxcin());
//                            muiTiem.setDonViTiem(objects.getAddressInjection());
//                            muiTiem.setNgayTiem(DataUtil.convertStringToDate(objects.getInjectionDate(), Constants.COMMON.patternDateDanCu));
//                            muiTiem.setNgayTao(new Date());
//                            muiTiem.setNguoiNhap("SyncVacxin");
//                            muiTiemList.add(muiTiem);
//                        }
//                    }
//                    nguoiTiemRequet.setListMuiTiem(muiTiemList);
//                }
//            }
//            if(!DataUtil.isNullOrEmpty(erroCode)){
//                saveLog(null,dataType,isUpdate,fromDate,toDate,jsonResponse,erroCode);
//            }else {
//                NguoiTiem nguoiTiemSave =nguoiTiemRepository.save(nguoiTiemRequet);
//                if(null!=nguoiTiemSave){
//                    saveLog(nguoiTiemSave.getId(),dataType,isUpdate,fromDate,toDate,jsonResponse,erroCode);
//                }
//                return  nguoiTiemSave;
//            }
//        }
//        return null;
//    }
//    private String validateNguoiTiem(InjectionSyncResponse syncDataResponse){
//        if(syncDataResponse.getBirthDate().length()!=8){
//            return "5";
//        }else {
//            Date ngaySinh = DataUtil.convertStringToDate(syncDataResponse.getBirthDate(),Constants.COMMON.patternSqlDateDC);
//            if(ngaySinh==null) return "5";
//        }
//        return null;
//    }
//
//    public void saveLog(Long id,String dataType,boolean isUpdate,Date fromDate,Date toDate,String jsonResponse,String erroCode){
//        LogSynchronizedCitizenData citizenData = new LogSynchronizedCitizenData();
//        citizenData.setDataType(dataType);
//        citizenData.setPkId(id);
//        if(isUpdate){
//            citizenData.setPkActionType("UPDATE");
//        }else {
//            citizenData.setPkActionType("CREATE");
//        }
//        citizenData.setSyncDate(new Date());
//        citizenData.setRequestDateFrom(fromDate);
//        citizenData.setRequestDateTo(toDate);
//        citizenData.setRequestContent(jsonResponse);
//        if(!DataUtil.isNullOrEmpty(erroCode)){
//            citizenData.setErroCode(erroCode);
//            citizenData.setMessErro(messErro(erroCode));
//        }
//        logSyncDataJpaRepository.save(citizenData);
//    }
//
//    public static String getCharacterDataFromElement(Element e) {
//        Node child = e.getFirstChild();
//        if (child instanceof CharacterData) {
//            CharacterData cd = (CharacterData) child;
//            return cd.getData();
//        }
//        return "";
//    }
//    private String messErro(String erroCode){
//        String message ="";
//        switch (erroCode){
//            case "1":
//                message ="Thông tin tên vacxin null";
//                break;
//            case "2":
//                message="Thông tin tên vacxin không đúng";
//                break;
//            case "3":
//                message="Ngày tiêm không hợp lệ";
//                break;
//            case "4":
//                message="Không tồn tại số định danh";
//                break;
//            case "5":
//                message="Ngay tháng năm sinh không đúng định dạng";
//                break;
//            case "6":
//                message="Không tồn tại thông tin mũi tiêm 1";
//                break;
//            case "7":
//                message="không có tiền nhận";
//                break;
//            case "8":
//                message="ngày nhận không có hoặc k đúng định dạng";
//                break;
//            case "9":
//                message="Số mũi tiêm không đúng định dạng";
//                break;
//            default:
//                message ="";
//        }
//        return message;
//    }
//    private String vaildateDataTiem(InjectionResponse injectionResponse){
//        try {
//            Long.valueOf(injectionResponse.getInjectionOrder());
//        }catch (Exception e){
//            logger.error(e.getMessage(),e);
//            return "9";
//        }
//        if(DataUtil.isNullOrEmpty(injectionResponse.getVaxcinName())){
//            return "1";// thong tin ten vacxin null
//        }else {
//            List<String> dmVacxin = new ArrayList<>();
//            dmVacxin.add("ASTRAZENECA");
//            dmVacxin.add("ASTRA ZENECA");
//            dmVacxin.add("GAM-COVID-VAC");
//            dmVacxin.add("SPUTNIKV");
//            dmVacxin.add("SPUTNIK-V");
//            dmVacxin.add("MODERNA");
//            dmVacxin.add("PFIZER");
//            dmVacxin.add("COMIRNATY");
//            dmVacxin.add("VERO CELL");
//            dmVacxin.add("VEROCELL");
//            dmVacxin.add("VERO-CELL");
//            dmVacxin.add("VEROCELL");
//            dmVacxin.add("HAYAT-VAX");
//            dmVacxin.add("HAYAT VAX");
//            dmVacxin.add("ABDALA");
//            dmVacxin.add("JANSSEN");
//
//            //check ten vacxin trong danh muc co dung chuan khong
//            List<String> filtList = dmVacxin.stream().
//                    filter(value -> injectionResponse.getVaxcinName().toUpperCase().contains(value)//convert to uppercase for checking
//                    ).//filter values containing black
//                    collect(Collectors.toList());//collect as list
//            if(DataUtil.isNullOrEmpty(filtList)){
//                return "2";// thong tin ten vac xin k dung
//            }
//        }
//        if(null==DataUtil.convertStringToDate(injectionResponse.getInjectionDate(), Constants.COMMON.patternDateDanCu)){
//            return "3"; // ngay thang nam tiem chung ko hop le
//        }
//        return "";
//    }
//
//}
//
