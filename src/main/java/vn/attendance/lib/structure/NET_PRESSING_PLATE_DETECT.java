package vn.attendance.lib.structure;


import vn.attendance.lib.NetSDKLib;

/** 
* @author 291189
* @description  压板检测结果 
* @date 2022/06/28 19:44:56
*/
public class NET_PRESSING_PLATE_DETECT extends NetSDKLib.SdkStructure {
/** 
压板状态 {@link vn.attendance.lib.enumeration.EM_PRESSING_PLATE_STATE}
*/
public			int					emPressingPlateState;
/** 
包围盒
*/
public NET_RECT stuBoundingBox=new NET_RECT();

public			NET_PRESSING_PLATE_DETECT(){
}
}