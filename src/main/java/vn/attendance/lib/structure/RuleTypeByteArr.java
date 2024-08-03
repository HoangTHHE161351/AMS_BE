package vn.attendance.lib.structure;

import vn.attendance.lib.NetSDKLib;

/**
 * @author 251823
 * @description 规则类型字节数组
 * @date 2021/09/27
 */
public class RuleTypeByteArr extends NetSDKLib.SdkStructure{
	
	/**
	 * 单个规则类型
	 */
	public byte[] szRuleTypeByteArr = new byte[32];

}
