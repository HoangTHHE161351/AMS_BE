package vn.attendance.lib.Enum;

/**
 * 智能分析事件类型枚举类
 */
public enum EM_EVENT_IVS {

    /**
     *订阅所有事件
     */
    EVENT_IVS_ALL(0x00000001),
    /**
     * 警戒线事件(对应 DEV_EVENT_CROSSLINE_INFO)
      */
    EVENT_IVS_CROSSLINEDETECTION(0x00000002),
    /**
     *警戒区事件(对应 DEV_EVENT_CROSSREGION_INFO)
     */
    EVENT_IVS_CROSSREGIONDETECTION(0x00000003),
    /**
     *物品遗留事件(对应 DEV_EVENT_LEFT_INFO)
     */
    EVENT_IVS_LEFTDETECTION(0x00000005),
    /**
     *停留事件(对应 DEV_EVENT_STAY_INFO)
     */
    EVENT_IVS_STAYDETECTION(0x00000006),
    /**
     *徘徊事件(对应  DEV_EVENT_WANDER_INFO)
     */
    EVENT_IVS_WANDERDETECTION(0x00000007),
    /**
     *移动事件(对应 DEV_EVENT_MOVE_INFO)
     */
    EVENT_IVS_MOVEDETECTION(0x00000009),
    /**
     *聚众事件(对应 DEV_EVENT_RIOTERL_INFO)
     */
    EVENT_IVS_RIOTERDETECTION(0x0000000B),
    /**
     *斗殴事件(对应 DEV_EVENT_FIGHT_INFO)
     */
    EVENT_IVS_FIGHTDETECTION(0x0000000E),
    /**
     *交通路口事件----老规则(对应 DEV_EVENT_TRAFFICJUNCTION_INFO)
     */
    EVENT_IVS_TRAFFICJUNCTION       (0x00000017),
    /**
     *交通卡口事件----老规则(对应 DEV_EVENT_TRAFFICGATE_INFO)
     */
    EVENT_IVS_TRAFFICGATE           (0x00000018),
    /**
     *人脸检测事件 (对应 DEV_EVENT_FACEDETECT_INFO)(智能规则对应  EVENT_IVS_FACEDETECT)
     */
    EVENT_IVS_FACEDETECT            (0x0000001A),
    /**
     * 交通拥堵事件(对应 DEV_EVENT_TRAFFICJAM_INFO)
     */
    EVENT_IVS_TRAFFICJAM            (0x0000001B),
    /**
     *交通违章-闯红灯事件(对应 DEV_EVENT_TRAFFIC_RUNREDLIGHT_INFO)
     */
    EVENT_IVS_TRAFFIC_RUNREDLIGHT   (0x00000100),
    /**
     * 交通违章-压车道线事件(对应 DEV_EVENT_TRAFFIC_OVERLINE_INFO)
     */
    EVENT_IVS_TRAFFIC_OVERLINE      (0x00000101),
    /**
     * 交通违章-逆行事件(对应  DEV_EVENT_TRAFFIC_RETROGRADE_INFO)
     */
    EVENT_IVS_TRAFFIC_RETROGRADE    (0x00000102),
    /**
     * 交通违章-违章左转(对应 DEV_EVENT_TRAFFIC_TURNLEFT_INFO)
     */
    EVENT_IVS_TRAFFIC_TURNLEFT      (0x00000103),
    /**
     * 交通违章-违章右转(对应 DEV_EVENT_TRAFFIC_TURNRIGHT_INFO)
     */
    EVENT_IVS_TRAFFIC_TURNRIGHT     (0x00000104),
    /**
     * 交通违章-违章掉头(对应 DEV_EVENT_TRAFFIC_UTURN_INFO)
     */
    EVENT_IVS_TRAFFIC_UTURN         (0x00000105),
    /**
     *交通违章-超速(对应 DEV_EVENT_TRAFFIC_OVERSPEED_INFO)
     */
    EVENT_IVS_TRAFFIC_OVERSPEED     (0x00000106),
    /**
     *交通违章-低速(对应 DEV_EVENT_TRAFFIC_UNDERSPEED_INFO)
     */
    EVENT_IVS_TRAFFIC_UNDERSPEED    (0x00000107),
    /**
     *交通违章-违章停车(对应 DEV_EVENT_TRAFFIC_PARKING_INFO)
     */
    EVENT_IVS_TRAFFIC_PARKING       (0x00000108),
    /**
     *交通违章-不按车道行驶(对应 DEV_EVENT_TRAFFIC_WRONGROUTE_INFO)
     */
    EVENT_IVS_TRAFFIC_WRONGROUTE    (0x00000109),
    /**
     * 交通违章-违章变道(对应 DEV_EVENT_TRAFFIC_CROSSLANE_INFO)
     */
    EVENT_IVS_TRAFFIC_CROSSLANE     (0x0000010A),
    /**
     * 交通违章-压黄线 (对应 DEV_EVENT_TRAFFIC_OVERYELLOWLINE_INFO)
     */
    EVENT_IVS_TRAFFIC_OVERYELLOWLINE(0x0000010B),
    /**
     * 交通违章-黄牌车占道事件(对应 DEV_EVENT_TRAFFIC_YELLOWPLATEINLANE_INFO)
     */
    EVENT_IVS_TRAFFIC_YELLOWPLATEINLANE  (0x0000010E),
    /**
     *交通违章-斑马线行人优先事件(对应 DEV_EVENT_TRAFFIC_PEDESTRAINPRIORITY_INFO)
     */
    EVENT_IVS_TRAFFIC_PEDESTRAINPRIORITY (0x0000010F),
    /**
     * 交通违章-禁止通行事件(对应 DEV_EVENT_TRAFFIC_NOPASSING_INFO)
     */
    EVENT_IVS_TRAFFIC_NOPASSING          (0x00000111),
    /**
     *异常奔跑事件(对应 DEV_EVENT_ABNORMALRUNDETECTION_INFO)
     */
    EVENT_IVS_ABNORMALRUNDETECTION 		 (0x00000112),
    /**
     * 物品搬移事件(对应 DEV_EVENT_TAKENAWAYDETECTION_INFO)
     */
    EVENT_IVS_TAKENAWAYDETECTION   		 (0x00000115),
    /**
     * 非法停车事件(对应 DEV_EVENT_PARKINGDETECTION_INFO)
     */
    EVENT_IVS_PARKINGDETECTION     		 (0x00000116),
    /**
     *目标识别事件(对应 DEV_EVENT_FACERECOGNITION_INFO, (对应的智能规则配置  CFG_FACERECOGNITION_INFO)
     */
    EVENT_IVS_FACERECOGNITION            (0x00000117),
    /**
     * 交通手动抓拍事件(对应  DEV_EVENT_TRAFFIC_MANUALSNAP_INFO)
     */
    EVENT_IVS_TRAFFIC_MANUALSNAP         (0x00000118),
    /**
     *交通流量统计事件(对应 DEV_EVENT_TRAFFIC_FLOW_STATE)
     */
    EVENT_IVS_TRAFFIC_FLOWSTATE          (0x00000119),
    /**
     *有车占道事件(对应 DEV_EVENT_TRAFFIC_VEHICLEINROUTE_INFO)
     */
    EVENT_IVS_TRAFFIC_VEHICLEINROUTE     (0x0000011B),
    /**
     *外部报警事件(对应 DEV_EVENT_ALARM_INFO)
     */
    EVENT_ALARM_LOCALALARM         		 (0x0000011D),
    /**
     *交通违章--卡口事件----新规则(对应 DEV_EVENT_TRAFFICJUNCTION_INFO)
     */
    EVENT_IVS_TRAFFIC_TOLLGATE           (0x00000120),
    /**
     *交通违章--占用公交车道事件(对应 DEV_EVENT_TRAFFIC_VEHICLEINBUSROUTE_INFO)
     */
    EVENT_IVS_TRAFFIC_VEHICLEINBUSROUTE  (0x00000124),
    /**
     * 交通违章--违章倒车事件(对应 DEV_EVENT_IVS_TRAFFIC_BACKING_INFO)
     */
    EVENT_IVS_TRAFFIC_BACKING            (0x00000125),
    /**
     * 声音异常检测(对应 DEV_EVENT_IVS_AUDIO_ABNORMALDETECTION_INFO)
     */
    EVENT_IVS_AUDIO_ABNORMALDETECTION    (0x00000126),
    /**
     * 交通违章-闯黄灯事件(对应 DEV_EVENT_TRAFFIC_RUNYELLOWLIGHT_INFO)
     */
    EVENT_IVS_TRAFFIC_RUNYELLOWLIGHT     (0x00000127),
    /**
     * 攀高检测事件(对应 DEV_EVENT_IVS_CLIMB_INFO)
     */
    EVENT_IVS_CLIMBDETECTION             (0x00000128),
    /**
     * 离岗检测事件(对应 DEV_EVENT_IVS_LEAVE_INFO)
     */
    EVENT_IVS_LEAVEDETECTION             (0x00000129),
    /**
     *交通违章--黄网格线抓拍事件(对应 DEV_EVENT_TRAFFIC_PARKINGONYELLOWBOX_INFO)
     */
    EVENT_IVS_TRAFFIC_PARKINGONYELLOWBOX (0x0000012A),
    /**
     *车位有车事件(对应 DEV_EVENT_TRAFFIC_PARKINGSPACEPARKING_INFO )
     */
    EVENT_IVS_TRAFFIC_PARKINGSPACEPARKING   (0x0000012B),
    /**
     * 车位无车事件(对应  DEV_EVENT_TRAFFIC_PARKINGSPACENOPARKING_INFO )
     */
    EVENT_IVS_TRAFFIC_PARKINGSPACENOPARKING (0x0000012C),
    /**
     * 交通行人事件(对应 DEV_EVENT_TRAFFIC_PEDESTRAIN_INFO)
     */
    EVENT_IVS_TRAFFIC_PEDESTRAIN        (0x0000012D),
    /**
     * 交通抛洒物品事件(对应 DEV_EVENT_TRAFFIC_THROW_INFO)
     */
    EVENT_IVS_TRAFFIC_THROW             (0x0000012E),
    /**
     *交通违章--压停止线事件(对应 DEV_EVENT_TRAFFIC_OVERSTOPLINE)
     */
    EVENT_IVS_TRAFFIC_OVERSTOPLINE      (0X00000137),
    /**
     *交通违章--交通未系安全带事件(对应 DEV_EVENT_TRAFFIC_WITHOUT_SAFEBELT)
     */
    EVENT_IVS_TRAFFIC_WITHOUT_SAFEBELT  (0x00000138),
    /**
     *驾驶员抽烟事件(对应 DEV_EVENT_TRAFFIC_DRIVER_SMOKING)
     */
    EVENT_IVS_TRAFFIC_DRIVER_SMOKING 	(0x00000139),
    /**
     * 驾驶员打电话事件(对应 DEV_EVENT_TRAFFIC_DRIVER_CALLING)
     */
    EVENT_IVS_TRAFFIC_DRIVER_CALLING 	(0x0000013A),
    /**
     * 交通违章--未按规定依次通行(对应 DEV_EVENT_TRAFFIC_PASSNOTINORDER_INFO)
     */
    EVENT_IVS_TRAFFIC_PASSNOTINORDER    (0x0000013C),
    /**
     *视频遮挡事件(对应 DEV_EVENT_ALARM_VIDEOBLIND)
     */
    EVENT_ALARM_VIDEOBLIND         		(0x00000153),
    /**
     *交通违章--车辆拥堵禁入事件(对应 DEV_EVENT_ALARM_JAMFORBIDINTO_INFO)
     */
    EVENT_IVS_TRAFFIC_JAM_FORBID_INTO	(0x00000163),
    /**
     *加油站提枪、挂枪事件(对应  DEV_EVENT_TRAFFIC_FCC_INFO)
     */
    EVENT_IVS_TRAFFIC_FCC               (0x0000016B),
    /**
     *门禁事件 (对应 DEV_EVENT_ACCESS_CTL_INFO)
     */
    EVENT_IVS_ACCESS_CTL                (0x00000204),
    /**
     *SnapManual事件(对应 DEV_EVENT_SNAPMANUAL)
     */
    EVENT_IVS_SNAPMANUAL                (0x00000205),
    /**
     *生理疲劳驾驶事件(对应 DEV_EVENT_TIREDPHYSIOLOGICAL_INFO)
     */
    EVENT_IVS_TRAFFIC_TIREDPHYSIOLOGICAL(0x00000207),
    /**
     * 人证比对事件(对应  DEV_EVENT_CITIZEN_PICTURE_COMPARE_INFO )
     */
    EVENT_IVS_CITIZEN_PICTURE_COMPARE   (0x00000209),
    /**
     *人体特征事件(对应 DEV_EVENT_HUMANTRAIT_INFO)
     */
    EVENT_IVS_HUMANTRAIT                (0x00000215),
    /**
     * 开车低头报警事件(对应DEV_EVENT_TIREDLOWERHEAD_INFO)
     */
    EVENT_IVS_TRAFFIC_TIREDLOWERHEAD 	(0x0000020A),
    /**
     *开车左顾右盼报警事件(对应DEV_EVENT_DRIVERLOOKAROUND_INFO)
     */
    EVENT_IVS_TRAFFIC_DRIVERLOOKAROUND 	(0x0000020B),
    /**
     *开车离岗报警事件(对应DEV_EVENT_DRIVERLEAVEPOST_INFO)
     */
    EVENT_IVS_TRAFFIC_DRIVERLEAVEPOST 	(0x0000020C),
    /**
     *开车打哈欠事件(对应DEV_EVENT_DRIVERYAWN_INFO)
     */
    EVENT_IVS_TRAFFIC_DRIVERYAWN   		(0x00000210),
    /**
     *人脸分析事件 (暂未有具体事件)
     */
    EVENT_IVS_FACEANALYSIS              (0x00000217),
    /**
     *车辆超速报警事件(对应 DEV_EVENT_HIGHSPEED_INFO)
     */
    EVENT_IVS_HIGHSPEED            		(0x0000022B),
    /**
     *车牌对比事件(智慧加油站项目)(对应 DEV_EVENT_VEHICLE_RECOGNITION_INFO)
     */
    EVENT_IVS_VEHICLE_RECOGNITION       (0x00000231),

    /**
     *人群密度检测事件(对应结构体 DEV_EVENT_CROWD_DETECTION_INFO)
     */
    EVENT_IVS_CROWDDETECTION		    (0x0000022C),
    /**
     *立体视觉区域内人数统计事件(对应DEV_EVENT_MANNUM_DETECTION_INFO)
     */
    EVENT_IVS_MAN_NUM_DETECTION         (0x0000020E),
    /**
     *电动扶梯运行异常事件 (对应DEV_EVENT_ELEVATOR_ABNORMAL_INFO)
     */
    EVENT_IVS_ELEVATOR_ABNORMAL         (0x0000023D),
    /**
     *IVSS人脸检测事件 (暂未有具体事件)
     */
    EVENT_IVSS_FACEATTRIBUTE            (0x00000243),
    /**
     *IVSS目标识别事件 (暂未有具体事件)
     */
    EVENT_IVSS_FACECOMPARE              (0x00000244),
    /**
     *火警事件(对应 DEV_EVENT_FIREWARNING_INFO)
     */
    EVENT_IVS_FIREWARNING				(0x00000245),
    /**
     *车道偏移预警(对应 DEV_EVENT_LANEDEPARTURE_WARNNING_INFO)
     */
    EVENT_IVS_LANEDEPARTURE_WARNNING 	(0X00000251),
    /**
     * 前向碰撞预警(对应 DEV_EVENT_FORWARDCOLLISION_WARNNING_INFO)
     */
    EVENT_IVS_FORWARDCOLLISION_WARNNING (0x00000252),
    /**
     * 漂浮物检测事件 (对应 DEV_EVENT_FLOATINGOBJECT_DETECTION_INFO)
     */
    EVENT_IVS_FLOATINGOBJECT_DETECTION	(0x00000257),
    /**
     * 打电话检测事件(对应 DEV_EVENT_PHONECALL_DETECT_INFO)
     */
    EVENT_IVS_PHONECALL_DETECT			(0x0000025A),
    /**
     *吸烟检测事件(对应 DEV_EVENT_SMOKING_DETECT_INFO)
     */
    EVENT_IVS_SMOKING_DETECT        	(0x0000025B),
    /**
     *雷达限速报警事件(对应 DEV_EVENT_RADAR_SPEED_LIMIT_ALARM_INFO)
     */
    EVENT_IVS_RADAR_SPEED_LIMIT_ALARM   (0x0000025C),
    /**
     *水位检测事件 (对应 DEV_EVENT_WATER_LEVEL_DETECTION_INFO)
     */
    EVENT_IVS_WATER_LEVEL_DETECTION		(0x0000025D),
    /**
     *城市机动车违停事件 (对应 DEV_EVENT_CITY_MOTORPARKING_INFO)
     */
    EVENT_IVS_CITY_MOTORPARKING			(0x0000024F),
    /**
     *城市机非动车违停事件 (对应 DEV_EVENT_CITY_NONMOTORPARKING_INFO)
     */
    EVENT_IVS_CITY_NONMOTORPARKING		(0x00000250),
    /**
     *违规撑伞检测事件 (对应 DEV_EVENT_HOLD_UMBRELLA_INFO)
     */
    EVENT_IVS_HOLD_UMBRELLA			    (0x0000025E),
    /**
     * 垃圾暴露检测事件 (对应 DEV_EVENT_GARBAGE_EXPOSURE_INFO)
     */
    EVENT_IVS_GARBAGE_EXPOSURE			(0x0000025F),
    /**
     *垃圾桶满溢检测事件 (对应 DEV_EVENT_DUSTBIN_OVER_FLOW_INFO)
     */
    EVENT_IVS_DUSTBIN_OVER_FLOW			(0x00000260),
    /**
     *门前脏乱检测事件 (对应 DEV_EVENT_DOOR_FRONT_DIRTY_INFO)
     */
    EVENT_IVS_DOOR_FRONT_DIRTY			(0x00000261),
    /**
     *排队滞留时间报警事件 (对应 DEV_EVENT_QUEUESTAY_DETECTION_INFO)
     */
    EVENT_IVS_QUEUESTAY_DETECTION		(0X00000262),
    /**
     *排队人数异常报警事件（对应 DEV_EVENT_QUEUENUM_DETECTION_INFO）
     */
    EVENT_IVS_QUEUENUM_DETECTION		(0X00000263),
    /**
     *生成图规则事件（对应 DEV_EVENT_GENERATEGRAPH_DETECTION_INFO）
     */
    EVENT_IVS_GENERATEGRAPH_DETECTION	(0X00000264),
    /**
     *交通违章-手动取证(对应  DEV_EVENT_TRAFFIC_PARKING_MANUAL_INFO)
     */
    EVENT_IVS_TRAFFIC_PARKING_MANUAL	(0x00000265),
    /**
     *安全帽检测事件(对应 DEV_EVENT_HELMET_DETECTION_INFO)
     */
    EVENT_IVS_HELMET_DETECTION      	(0x00000266),
    /**
     *包裹堆积程度检测事件(对应 DEV_EVENT_DEPOSIT_DETECTION_INFO)
     */
    EVENT_IVS_DEPOSIT_DETECTION      	(0x00000267),
    /**
     * 热点异常报警事件(对应 DEV_EVENT_HOTSPOT_WARNING_INFO)
     */
    EVENT_IVS_HOTSPOT_WARNING			(0x00000268),
    /**
     * 称重平台检测事件(对应 DEV_EVENT_WEIGHING_PLATFORM_DETECTION_INFO)
     */
    EVENT_IVS_WEIGHING_PLATFORM_DETECTION(0x00000269),
    /**
     * 课堂行为分析事件(对应 DEV_EVENT_CLASSROOM_BEHAVIOR_INFO)
     */
    EVENT_IVS_CLASSROOM_BEHAVIOR		(0x0000026A),
    /**
     *  工装(安全帽/工作服等)检测事件(对应 DEV_EVENT_WORKCLOTHES_DETECT_INFO)
     */
    EVENT_IVS_WORKCLOTHES_DETECT		(0x0000026E),
    /**
     *  立体视觉站立事件(对应DEV_EVENT_MANSTAND_DETECTION_INFO)
     */
    EVENT_IVS_MAN_STAND_DETECTION  		(0x0000020D),
    /**
     *  加油站车辆检测事件 (对应 DEV_EVENT_GASSTATION_VEHICLE_DETECT_INFO)
     */
    EVENT_IVS_GASSTATION_VEHICLE_DETECT (0x00000283),
    /**
     *  商铺占道经营事件(对应 DEV_EVENT_SHOPPRESENCE_INFO)
     */
    EVENT_IVS_SHOPPRESENCE				(0x00000246),
    /**
     *  流动摊贩事件 (对应 DEV_EVENT_FLOWBUSINESS_INFO)
     */
    EVENT_IVS_FLOWBUSINESS				(0x0000024E),
    /**
     *  行人卡口事件(对应 DEV_EVENT_PEDESTRIAN_JUNCTION_INFO)
     */
    EVENT_IVS_PEDESTRIAN_JUNCTION		(0x00000230),
    /**
     *  拉横幅事件(对应 DEV_EVENT_BANNER_DETECTION_INFO)
     */
    EVENT_IVS_BANNER_DETECTION			(0x0000023B),
    /**
     *  智慧厨房穿着检测事件（对不戴口罩、厨师帽以及颜色不符合规定的厨师服进行报警）（对应 DEV_EVENT_SMART_KITCHEN_CLOTHES_DETECTION_INFO）
     */
    EVENT_IVS_SMART_KITCHEN_CLOTHES_DETECTION(0x0000029D),
    /**
     *  水位监测事件
     */
    EVENT_IVS_WATER_STAGE_MONITOR       (0x0000030A),
    /**
     *  暴力抛物检测(对应 DEV_EVENT_VIOLENT_THROW_DETECTION_INFO)
     */
    EVENT_IVS_VIOLENT_THROW_DETECTION	(0x0000027D),
    /**
     *  人体温智能检测事件(对应 DEV_EVENT_ANATOMY_TEMP_DETECT_INFO)
     */
    EVENT_IVS_ANATOMY_TEMP_DETECT		(0x00000303),
    /**
     *  起雾检测事件(对应 DEV_EVENT_FOG_DETECTION)
     */
    EVENT_IVS_FOG_DETECTION				(0x00000308),
    /**
     *  视频异常事件(对应 DEV_EVENT_VIDEOABNORMALDETECTION_INFO)
     */
    EVENT_IVS_VIDEOABNORMALDETECTION    (0x00000013),
    /**
     *  单人独处事件(对应 DEV_EVENT_STAY_ALONE_DETECTION_INFO)
     */
    EVENT_IVS_STAY_ALONE_DETECTION      (0x00000270),
    /**
     *  囚犯起身事件(对应 DEV_EVENT_PSRISEDETECTION_INFO)
     */
    EVENT_IVS_PSRISEDETECTION     (0x0000011E),
    /**
     *  高空抛物检测(对应DEV_EVENT_HIGH_TOSS_DETECT_INFO)
     */
    EVENT_IVS_HIGH_TOSS_DETECT          (0x0000028D),

    /**
     * 特征提取事件(对应 DEV_EVENT_FEATURE_ABSTRACT_INFO)
     */
    EVENT_IVS_FEATURE_ABSTRACT          (0x00000276),
	/***
	 * 烟雾报警事件(对应 DEV_EVENT_SMOKE_INFO)
	 */
	EVENT_IVS_SMOKEDETECTION            (0x0000000D),      
	/**
	 * 火警事件(对应 DEV_EVENT_FIRE_INFO)
	 */
	EVENT_IVS_FIREDETECTION             (0x0000000C), 
	/**
	 * 交通路障检测事件(对应 DEV_EVENT_TRAFFIC_ROAD_BLOCK_INFO)
	 */
	EVENT_IVS_TRAFFIC_ROAD_BLOCK        (0x00000271),	
	/**
	 * 交通道路施工检测事件(对应 DEV_EVENT_TRAFFIC_ROAD_CONSTRUCTION_INFO)
	 */
	EVENT_IVS_TRAFFIC_ROAD_CONSTRUCTION  (0x00000272),
	/**
	 * 人员逆行事件(对应 DEV_EVENT_RETROGRADEDETECTION_INFO)
	 */
	EVENT_IVS_RETROGRADEDETECTION        (0x00000113), 
	/**
	 * 交通事故事件(对应 DEV_EVENT_TRAFFICACCIDENT_INFO)
	 */
	EVENT_IVS_TRAFFICACCIDENT            (0x00000016);       
    private final int id;
    private EM_EVENT_IVS(int id){
        this.id=id;
    }

    public int getId() {
        return id;
    }

}
