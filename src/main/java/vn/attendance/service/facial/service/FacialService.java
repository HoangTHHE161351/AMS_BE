package vn.attendance.service.facial.service;

import org.apache.commons.math3.analysis.function.Add;
import vn.attendance.exception.AmsException;
import vn.attendance.service.facial.request.AddFacialRequest;
import vn.attendance.service.facial.request.SetFaceRequest;
import vn.attendance.service.facial.response.FacialResponseDto;

import java.util.List;

public interface FacialService {

    List<FacialResponseDto> getFacialByUserId(Integer userId) throws AmsException;


    AddFacialRequest addFacialData(AddFacialRequest request, Integer userId) throws AmsException;

    String deleteFacialData(List<Integer> requestList, Integer userId) throws AmsException;

    void setFacialData(SetFaceRequest request) throws AmsException;
}
