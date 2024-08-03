package vn.attendance.lib.enumeration;

/**
 * @author 251823
 * @description 智能交通道闸故障代码
 * @date 2020/12/14
 */
public enum EM_TRAFFIC_SNAP_STROBE_FAULT_CODE_TYPE {
	// 未知
	EM_TRAFFIC_SNAP_STROBE_FAULT_CODE_TYPE_UNKNOWN(0, "未知"),
	// 编码器信号异常
	EM_TRAFFIC_SNAP_STROBE_FAULT_CODE_TYPE_ENCODER_SIGNAL_EXCEPTION(1, "编码器信号异常"),
	// 电机运行超时
	EM_TRAFFIC_SNAP_STROBE_FAULT_CODE_TYPE_MOTOR_RUNNING_OVERTIME(2, "电机运行超时"),
	// 电机堵转
	EM_TRAFFIC_SNAP_STROBE_FAULT_CODE_TYPE_MOTOR_STALLING(3, "电机堵转"),
	// 红外线被物体挡住
	EM_TRAFFIC_SNAP_STROBE_FAULT_CODE_TYPE_INFRARED_IS_BLOCKED_BY_OBJECTS(4, "红外线被物体挡住"),
	// 压力波被物体阻挡
	EM_TRAFFIC_SNAP_STROBE_FAULT_CODE_TYPE_THE_PRESSURE_WAVE_IS_BLOCKED_BY_AN_OBJECT(5, "压力波被物体阻挡"),
	// 逆变单元保护
	EM_TRAFFIC_SNAP_STROBE_FAULT_CODE_TYPE_INVERTER_UNIT_PROTECTION(6, "逆变单元保护"),
	// 加速过流
	EM_TRAFFIC_SNAP_STROBE_FAULT_CODE_TYPE_ACCELERATED_OVERCURRENT(7, "加速过流"),
	// 减速过流
	EM_TRAFFIC_SNAP_STROBE_FAULT_CODE_TYPE_DECELERATION_OVER_CURRENT(8, "减速过流"),
	// 恒速过流
	EM_TRAFFIC_SNAP_STROBE_FAULT_CODE_TYPE_CONSTANT_SPEED_OVERCURRENT(9, "恒速过流"),
	// 加速过压
	EM_TRAFFIC_SNAP_STROBE_FAULT_CODE_TYPE_ACCELERATED_OVERVOLTAGE(10, "加速过压"),
	// 减速过压
	EM_TRAFFIC_SNAP_STROBE_FAULT_CODE_TYPE_DECELERATION_OVERPRESSURE(11, "减速过压"),
	// 恒速过压
	EM_TRAFFIC_SNAP_STROBE_FAULT_CODE_TYPE_CONSTANT_SPEED_OVERVOLTAGE(12, "恒速过压"),
	// 制动电源异常
	EM_TRAFFIC_SNAP_STROBE_FAULT_CODE_TYPE_BRAKING_POWER_SUPPLY_EXCEPTION(13, "制动电源异常"),
	// 欠压故障
	EM_TRAFFIC_SNAP_STROBE_FAULT_CODE_TYPE_UNDER_VOLTAGE(14, "欠压故障"),
	// 变频器过载
	EM_TRAFFIC_SNAP_STROBE_FAULT_CODE_TYPE_FREQUENCY_CONVERTER_OVERLOAD(15, "变频器过载"),
	// 电机过载
	EM_TRAFFIC_SNAP_STROBE_FAULT_CODE_TYPE_MOTOR_OVERLOAD(16, "电机过载"),
	// 输入缺相
	EM_TRAFFIC_SNAP_STROBE_FAULT_CODE_TYPE_INPUT_PHASE_LOSS(17, "输入缺相"),
	// 输出缺相
	EM_TRAFFIC_SNAP_STROBE_FAULT_CODE_TYPE_OUTPUT_PHASE_LOSS(18, "输出缺相"),
	// 模块过热
	EM_TRAFFIC_SNAP_STROBE_FAULT_CODE_TYPE_MODULE_OVERHEAT(19, "模块过热"),
	// 外部故障
	EM_TRAFFIC_SNAP_STROBE_FAULT_CODE_TYPE_EXTERNAL_FAULT(20, "外部故障"),
	// 继电器异常
	EM_TRAFFIC_SNAP_STROBE_FAULT_CODE_TYPE_RELAY_EXCEPTION(21, "继电器异常"),
	// 电流检测异常
	EM_TRAFFIC_SNAP_STROBE_FAULT_CODE_TYPE_CURRENT_DETECTION_EXCEPTION(22, "电流检测异常"),
	// 电机调谐异常
	EM_TRAFFIC_SNAP_STROBE_FAULT_CODE_TYPE_MOTOR_TUNING_EXCEPTION(23, "电机调谐异常"),
	// EEPROM 读写异常
	EM_TRAFFIC_SNAP_STROBE_FAULT_CODE_TYPE_EEPROM_READ_WRITE_EXCEPTION(24, "EEPROM 读写异常"),
	// 变频器硬件故障
	EM_TRAFFIC_SNAP_STROBE_FAULT_CODE_TYPE_INVERTER_HARDWARE_FAILURE(25, "变频器硬件故障"),
	// 电机对地短路
	EM_TRAFFIC_SNAP_STROBE_FAULT_CODE_TYPE_MOTOR_SHORT_CIRCUIT_TO_GROUND(26, "电机对地短路"),
	// 运行超时
	EM_TRAFFIC_SNAP_STROBE_FAULT_CODE_TYPE_RUN_TIMEOUT(27, "运行超时"),
	// 开闸运行时遇阻
	EM_TRAFFIC_SNAP_STROBE_FAULT_CODE_TYPE_THE_GATE_IS_BLOCKED(28, "开闸运行时遇阻"),
	// 传感器故障
	EM_TRAFFIC_SNAP_STROBE_FAULT_CODE_TYPE_SENSOR(29, "传感器故障"),
	// 限位故障
	EM_TRAFFIC_SNAP_STROBE_FAULT_CODE_TYPE_LIMIT(30, "限位故障"),
	// 内部通讯故障
	EM_TRAFFIC_SNAP_STROBE_FAULT_CODE_TYPE_INTERNAL_COMMUNICATION_FAULT(31, "内部通讯故障");

	private int value;
	private String note;

	private EM_TRAFFIC_SNAP_STROBE_FAULT_CODE_TYPE(int givenValue, String note) {
		this.value = givenValue;
		this.note = note;
	}

	public String getNote() {
		return note;
	}

	public int getValue() {
		return value;
	}

	public static String getNoteByValue(int givenValue) {
		for (EM_TRAFFIC_SNAP_STROBE_FAULT_CODE_TYPE enumType : EM_TRAFFIC_SNAP_STROBE_FAULT_CODE_TYPE.values()) {
			if (givenValue == enumType.getValue()) {
				return enumType.getNote();
			}
		}
		return null;
	}

	public static int getValueByNote(String givenNote) {
		for (EM_TRAFFIC_SNAP_STROBE_FAULT_CODE_TYPE enumType : EM_TRAFFIC_SNAP_STROBE_FAULT_CODE_TYPE.values()) {
			if (givenNote.equals(enumType.getNote())) {
				return enumType.getValue();
			}
		}
		return -1;
	}
}
