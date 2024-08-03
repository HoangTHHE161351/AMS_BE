package vn.attendance.lib.structure;


import vn.attendance.lib.NetSDKLib;

/**
 * @author 260611
 * @description 云台可视域状态结构
 * @date 2022/10/13 16:01:59
 */
public class DH_OUT_PTZ_VIEW_RANGE_STATUS extends NetSDKLib.SdkStructure {

    public int dwSize;
    /**
     * 可视距离, 单位:米
     */
    public double dbDistance;
    /**
     * 水平可视角度, 0~1800, 单位:十分之一度
     */
    public int nAngelH;
    /**
     * 水平方位角度, 0~3600, 单位:十分之一度
     */
    public int nAzimuthH;
    /**
     * 水平倾斜角度, -900~900, 单位:十分之一度
     */
    public int nInclinationH;
    /**
     * 垂直可视角度, 0~1800, 单位:十分之一度
     */
    public int nAngelV;
    /**
     * 垂直方位角度, 0~3600, 单位:十分之一度
     */
    public int nAzimuthV;

    public DH_OUT_PTZ_VIEW_RANGE_STATUS() {
        this.dwSize = this.size();
    }

    @Override
    public String toString() {
        return "DH_OUT_PTZ_VIEW_RANGE_STATUS{" +
                "dwSize=" + dwSize +
                ", dbDistance=" + dbDistance +
                ", nAngelH=" + nAngelH +
                ", nAzimuthH=" + nAzimuthH +
                ", nInclinationH=" + nInclinationH +
                ", nAngelV=" + nAngelV +
                ", nAzimuthV=" + nAzimuthV +
                '}';
    }
}