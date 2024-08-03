package vn.attendance.lib.structure;


import vn.attendance.lib.NetSDKLib;

/**
 * @author 260611
 * @description 雷达开始升级通知入参(对应 EM_RADAR_OPERATE_TYPE_STARTUPGRADE)
 * @date 2022/08/04 10:13:31
 */
public class NET_IN_RADAR_STARTUPGRADE extends NetSDKLib.SdkStructure {
    /**
     * 结构体大小
     */
    public int dwSize;

    public NET_IN_RADAR_STARTUPGRADE() {
        this.dwSize = this.size();
    }
}