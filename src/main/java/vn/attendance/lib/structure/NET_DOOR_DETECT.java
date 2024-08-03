package vn.attendance.lib.structure;


import vn.attendance.lib.NetSDKLib;

/** 
* @author 291189
* @description  箱门检测结果 
* @date 2022/06/28 19:44:55
*/
public class NET_DOOR_DETECT extends NetSDKLib.SdkStructure {
/** 
箱门状态 {@link vn.attendance.lib.enumeration.EM_DOOR_STATE}
*/
public			int					emDoorState;
/** 
包围盒
*/
public NET_RECT stuBoundingBox=new NET_RECT();

public NET_DOOR_DETECT(){
}
}