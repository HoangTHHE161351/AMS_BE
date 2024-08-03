package vn.attendance.lib.enumeration;


import vn.attendance.lib.NetSDKLib;

/**
 * @author 47081
 * @version 1.0
 * @description netSdk错误类型枚举
 * @since 2020/7/28
 */
public enum ENUMERROR {
    NET_UNDEFIND(-2, "没有定义"),
    NET_NOERROR(0, "没有错误"),
    NET_ERROR(-1, "未知错误"),
    NET_SYSTEM_ERROR(1, "Windows系统出错"),
    NET_NETWORK_ERROR(2, "网络错误，可能是因为网络超时"),
    NET_DEV_VER_NOMATCH(3, "设备协议不匹配"),
    NET_INVALID_HANDLE(4, "句柄无效"),
    NET_OPEN_CHANNEL_ERROR(5, "打开通道失败"),
    NET_CLOSE_CHANNEL_ERROR(6, "关闭通道失败"),
    NET_ILLEGAL_PARAM(7, "用户参数不合法"),
    NET_SDK_INIT_ERROR(8, "SDK初始化出错"),
    NET_SDK_UNINIT_ERROR(9, "SDK清理出错"),
    NET_RENDER_OPEN_ERROR(10, "申请render资源出错"),
    NET_DEC_OPEN_ERROR(11, "打开解码库出错"),
    NET_DEC_CLOSE_ERROR(12, "关闭解码库出错"),

    NET_MULTIPLAY_NOCHANNEL(13, "多画面预览中检测到通道数为0"),
    NET_TALK_INIT_ERROR(14, "录音库初始化失败"),
    NET_TALK_NOT_INIT(15, "录音库未经初始化"),
    NET_TALK_SENDDATA_ERROR(16, "发送音频数据出错"),
    NET_REAL_ALREADY_SAVING(17, "实时数据已经处于保存状态"),
    NET_NOT_SAVING(18, "未保存实时数据"),
    NET_OPEN_FILE_ERROR(19, "打开文件出错"),
    NET_PTZ_SET_TIMER_ERROR(20, "启动云台控制定时器失败"),
    NET_RETURN_DATA_ERROR(21, "对返回数据的校验出错"),
    NET_INSUFFICIENT_BUFFER(22, "没有足够的缓存"),
    NET_NOT_SUPPORTED(23, "当前SDK未支持该功能"),
    NET_NO_RECORD_FOUND(24, "查询不到录像"),
    NET_NOT_AUTHORIZED(25, "无操作权限"),
    NET_NOT_NOW(26, "暂时无法执行"),
    NET_NO_TALK_CHANNEL(27, "未发现对讲通道"),
    NET_NO_AUDIO(28, "未发现音频"),
    NET_NO_INIT(29, "网络SDK未经初始化"),
    NET_DOWNLOAD_END(30, "下载已结束"),
    NET_EMPTY_LIST(31, "查询结果为空"),
    NET_ERROR_GETCFG_SYSATTR(32, "获取系统属性配置失败"),
    NET_ERROR_GETCFG_SERIAL(33, "获取序列号失败"),
    NET_ERROR_GETCFG_GENERAL(34, "获取常规属性失败"),
    NET_ERROR_GETCFG_DSPCAP(35, "获取DSP能力描述失败"),
    NET_ERROR_GETCFG_NETCFG(36, "获取网络配置失败"),
    NET_ERROR_GETCFG_CHANNAME(37, "获取通道名称失败"),
    NET_ERROR_GETCFG_VIDEO(38, "获取视频属性失败"),
    NET_ERROR_GETCFG_RECORD(39, "获取录象配置失败"),
    NET_ERROR_GETCFG_PRONAME(40, "获取解码器协议名称失败"),
    NET_ERROR_GETCFG_FUNCNAME(41, "获取232串口功能名称失败"),
    NET_ERROR_GETCFG_485DECODER(42, "获取解码器属性失败"),
    NET_ERROR_GETCFG_232COM(43, "获取232串口配置失败"),
    NET_ERROR_GETCFG_ALARMIN(44, "获取外部报警输入配置失败"),
    NET_ERROR_GETCFG_ALARMDET(45, "获取动态检测报警失败"),
    NET_ERROR_GETCFG_SYSTIME(46, "获取设备时间失败"),
    NET_ERROR_GETCFG_PREVIEW(47, "获取预览参数失败"),
    NET_ERROR_GETCFG_AUTOMT(48, "获取自动维护配置失败"),
    NET_ERROR_GETCFG_VIDEOMTRX(49, "获取视频矩阵配置失败"),
    NET_ERROR_GETCFG_COVER(50, "获取区域遮挡配置失败"),
    NET_ERROR_GETCFG_WATERMAKE(51, "获取图象水印配置失败"),
    NET_ERROR_GETCFG_MULTICAST(52, "获取配置失败位置：组播端口按通道配置"),
    NET_ERROR_SETCFG_GENERAL(55, "修改常规属性失败"),
    NET_ERROR_SETCFG_NETCFG(56, "修改网络配置失败"),
    NET_ERROR_SETCFG_CHANNAME(57, "修改通道名称失败"),
    NET_ERROR_SETCFG_VIDEO(58, "修改视频属性失败"),
    NET_ERROR_SETCFG_RECORD(59, "修改录象配置失败"),
    NET_ERROR_SETCFG_485DECODER(60, "修改解码器属性失败"),
    NET_ERROR_SETCFG_232COM(61, "修改232串口配置失败"),
    NET_ERROR_SETCFG_ALARMIN(62, "修改外部输入报警配置失败"),
    NET_ERROR_SETCFG_ALARMDET(63, "修改动态检测报警配置失败"),
    NET_ERROR_SETCFG_SYSTIME(64, "修改设备时间失败"),
    NET_ERROR_SETCFG_PREVIEW(65, "修改预览参数失败"),
    NET_ERROR_SETCFG_AUTOMT(66, "修改自动维护配置失败"),
    NET_ERROR_SETCFG_VIDEOMTRX(67, "修改视频矩阵配置失败"),
    NET_ERROR_SETCFG_COVER(68, "修改区域遮挡配置失败"),
    NET_ERROR_SETCFG_WATERMAKE(69, "修改图象水印配置失败"),
    NET_ERROR_SETCFG_WLAN(70, "修改无线网络信息失败"),
    NET_ERROR_SETCFG_WLANDEV(71, "选择无线网络设备失败"),
    NET_ERROR_SETCFG_REGISTER(72, "修改主动注册参数配置失败"),
    NET_ERROR_SETCFG_CAMERA(73, "修改摄像头属性配置失败"),
    NET_ERROR_SETCFG_INFRARED(74, "修改红外报警配置失败"),
    NET_ERROR_SETCFG_SOUNDALARM(75, "修改音频报警配置失败"),
    NET_ERROR_SETCFG_STORAGE(76, "修改存储位置配置失败"),
    NET_AUDIOENCODE_NOTINIT(77, "音频编码接口没有成功初始化"),
    NET_DATA_TOOLONGH(78, "数据过长"),
    NET_UNSUPPORTED(79, "设备不支持该操作"),
    NET_DEVICE_BUSY(80, "设备资源不足"),
    NET_SERVER_STARTED(81, "服务器已经启动"),
    NET_SERVER_STOPPED(82, "服务器尚未成功启动"),
    NET_LISTER_INCORRECT_SERIAL(83, "输入序列号有误"),
    NET_QUERY_DISKINFO_FAILED(84, "获取硬盘信息失败"),
    NET_ERROR_GETCFG_SESSION(85, "获取连接Session信息"),
    NET_USER_FLASEPWD_TRYTIME(86, "输入密码错误超过限制次数"),
    NET_LOGIN_ERROR_PASSWORD(100, "密码不正确"),
    NET_LOGIN_ERROR_USER(101, "帐户不存在"),
    NET_LOGIN_ERROR_TIMEOUT(102, "等待登录返回超时"),
    NET_LOGIN_ERROR_RELOGGIN(103, "帐号已登录"),
    NET_LOGIN_ERROR_LOCKED(104, "帐号已被锁定"),
    NET_LOGIN_ERROR_BLACKLIST(105, "帐号已被列为禁止名单"),
    NET_LOGIN_ERROR_BUSY(106, "资源不足，系统忙"),
    NET_LOGIN_ERROR_CONNECT(107, "登录设备超时，请检查网络并重试"),
    NET_LOGIN_ERROR_NETWORK(108, "网络连接失败"),
    NET_LOGIN_ERROR_SUBCONNECT(109, "登录设备成功，但无法创建视频通道，请检查网络状况"),
    NET_LOGIN_ERROR_MAXCONNECT(110, "超过最大连接数"),
    NET_LOGIN_ERROR_PROTOCOL3_ONLY(111, "只支持3代协议"),
    NET_LOGIN_ERROR_UKEY_LOST(112, "未插入U盾或U盾信息错误"),
    NET_LOGIN_ERROR_NO_AUTHORIZED(113, "客户端IP地址没有登录权限"),
    NET_LOGIN_ERROR_USER_OR_PASSOWRD(117, "账号或密码错误"),
    NET_LOGIN_ERROR_DEVICE_NOT_INIT(118, "设备尚未初始化，不能登录，请先初始化设备"),
    NET_RENDER_SOUND_ON_ERROR(120, "Render库打开音频出错"),
    NET_RENDER_SOUND_OFF_ERROR(121, "Render库关闭音频出错"),
    NET_RENDER_SET_VOLUME_ERROR(122, "Render库控制音量出错"),
    NET_RENDER_ADJUST_ERROR(123, "Render库设置画面参数出错"),
    NET_RENDER_PAUSE_ERROR(124, "Render库暂停播放出错"),
    NET_RENDER_SNAP_ERROR(125, "Render库抓图出错"),
    NET_RENDER_STEP_ERROR(126, "Render库步进出错"),
    NET_RENDER_FRAMERATE_ERROR(127, "Render库设置帧率出错"),
    NET_RENDER_DISPLAYREGION_ERROR(128, "Render库设置显示区域出错"),
    NET_RENDER_GETOSDTIME_ERROR(129, "Render库获取当前播放时间出错"),
    NET_GROUP_EXIST(140, "组名已存在"),
    NET_GROUP_NOEXIST(141, "组名不存在"),
    NET_GROUP_RIGHTOVER(142, "组的权限超出权限列表范围"),
    NET_GROUP_HAVEUSER(143, "组下有用户，不能删除"),
    NET_GROUP_RIGHTUSE(144, "组的某个权限被用户使用，不能删除"),
    NET_GROUP_SAMENAME(145, "新组名同已有组名重复"),
    NET_USER_EXIST(146, "用户已存在"),
    NET_USER_NOEXIST(147, "用户不存在"),
    NET_USER_RIGHTOVER(148, "用户权限超出组权限"),
    NET_USER_PWD(149, "保留帐号，不容许修改密码"),
    NET_USER_FLASEPWD(150, "密码不正确"),
    NET_USER_NOMATCHING(151, "密码不匹配"),
    NET_USER_INUSE(152, "账号正在使用中"),

    NET_ERROR_GETCFG_ETHERNET(300, "获取网卡配置失败"),
    NET_ERROR_GETCFG_WLAN(301, "获取无线网络信息失败"),
    NET_ERROR_GETCFG_WLANDEV(302, "获取无线网络设备失败"),
    NET_ERROR_GETCFG_REGISTER(303, "获取主动注册参数失败"),
    NET_ERROR_GETCFG_CAMERA(304, "获取摄像头属性失败"),
    NET_ERROR_GETCFG_INFRARED(305, "获取红外报警配置失败"),
    NET_ERROR_GETCFG_SOUNDALARM(306, "获取音频报警配置失败"),
    NET_ERROR_GETCFG_STORAGE(307, "获取存储位置配置失败"),
    NET_ERROR_GETCFG_MAIL(308, "获取邮件配置失败"),
    NET_CONFIG_DEVBUSY(309, "暂时无法设置"),
    NET_CONFIG_DATAILLEGAL(310, "配置数据不合法"),
    NET_ERROR_GETCFG_DST(311, "获取夏令时配置失败"),
    NET_ERROR_SETCFG_DST(312, "设置夏令时配置失败"),
    NET_ERROR_GETCFG_VIDEO_OSD(313, "获取视频OSD叠加配置失败"),
    NET_ERROR_SETCFG_VIDEO_OSD(314, "设置视频OSD叠加配置失败"),
    NET_ERROR_GETCFG_GPRSCDMA(315, "获取CDMA/GPRS网络配置失败"),
    NET_ERROR_SETCFG_GPRSCDMA(316, "设置CDMA/GPRS网络配置失败"),
    NET_ERROR_GETCFG_IPFILTER(317, "获取IP过滤配置失败"),
    NET_ERROR_SETCFG_IPFILTER(318, "设置IP过滤配置失败"),
    NET_ERROR_GETCFG_TALKENCODE(319, "获取语音对讲编码配置失败"),
    NET_ERROR_SETCFG_TALKENCODE(320, "设置语音对讲编码配置失败"),
    NET_ERROR_GETCFG_RECORDLEN(321, "获取录像打包长度配置失败"),
    NET_ERROR_SETCFG_RECORDLEN(322, "设置录像打包长度配置失败"),
    NET_DONT_SUPPORT_SUBAREA(323, "不支持网络硬盘分区"),
    NET_ERROR_GET_AUTOREGSERVER(324, "获取设备上主动注册服务器信息失败"),
    NET_ERROR_CONTROL_AUTOREGISTER(325, "主动注册重定向注册错误"),
    NET_ERROR_DISCONNECT_AUTOREGISTER(326, "断开主动注册服务器错误"),
    NET_ERROR_GETCFG_MMS(327, "获取mms配置失败"),
    NET_ERROR_SETCFG_MMS(328, "设置mms配置失败"),
    NET_ERROR_GETCFG_SMSACTIVATION(329, "获取短信激活无线连接配置失败"),
    NET_ERROR_SETCFG_SMSACTIVATION(330, "设置短信激活无线连接配置失败"),
    NET_ERROR_GETCFG_DIALINACTIVATION(331, "获取拨号激活无线连接配置失败"),
    NET_ERROR_SETCFG_DIALINACTIVATION(332, "设置拨号激活无线连接配置失败"),
    NET_ERROR_GETCFG_VIDEOOUT(333, "查询视频输出参数配置失败"),
    NET_ERROR_SETCFG_VIDEOOUT(334, "设置视频输出参数配置失败"),
    NET_ERROR_GETCFG_OSDENABLE(335, "获取osd叠加使能配置失败"),
    NET_ERROR_SETCFG_OSDENABLE(336, "设置osd叠加使能配置失败"),
    NET_ERROR_SETCFG_ENCODERINFO(337, "设置数字通道前端编码接入配置失败"),
    NET_ERROR_GETCFG_TVADJUST(338, "获取TV调节配置失败"),
    NET_ERROR_SETCFG_TVADJUST(339, "设置TV调节配置失败"),
    NET_ERROR_CONNECT_FAILED(340, "请求建立连接失败"),
    NET_ERROR_SETCFG_BURNFILE(341, "请求刻录文件上传失败"),
    NET_ERROR_SNIFFER_GETCFG(342, "获取抓包配置信息失败"),
    NET_ERROR_SNIFFER_SETCFG(343, "设置抓包配置信息失败"),
    NET_ERROR_DOWNLOADRATE_GETCFG(344, "查询下载限制信息失败"),
    NET_ERROR_DOWNLOADRATE_SETCFG(345, "设置下载限制信息失败"),
    NET_ERROR_SEARCH_TRANSCOM(346, "查询串口参数失败"),
    NET_ERROR_GETCFG_POINT(347, "获取预制点信息错误"),
    NET_ERROR_SETCFG_POINT(348, "设置预制点信息错误"),
    NET_SDK_LOGOUT_ERROR(349, "SDK没有正常登出设备"),
    NET_ERROR_GET_VEHICLE_CFG(350, "获取车载配置失败"),
    NET_ERROR_SET_VEHICLE_CFG(351, "设置车载配置失败"),
    NET_ERROR_GET_ATM_OVERLAY_CFG(352, "获取atm叠加配置失败"),
    NET_ERROR_SET_ATM_OVERLAY_CFG(353, "设置atm叠加配置失败"),
    NET_ERROR_GET_ATM_OVERLAY_ABILITY(354, "获取atm叠加能力失败"),
    NET_ERROR_GET_DECODER_TOUR_CFG(355, "获取解码器解码轮巡配置失败"),
    NET_ERROR_SET_DECODER_TOUR_CFG(356, "设置解码器解码轮巡配置失败"),
    NET_ERROR_CTRL_DECODER_TOUR(357, "控制解码器解码轮巡失败"),
    NET_GROUP_OVERSUPPORTNUM(358, "超出设备支持最大用户组数目"),
    NET_USER_OVERSUPPORTNUM(359, "超出设备支持最大用户数目"),
    NET_ERROR_GET_SIP_CFG(368, "获取SIP配置失败"),
    NET_ERROR_SET_SIP_CFG(369, "设置SIP配置失败"),
    NET_ERROR_GET_SIP_ABILITY(370, "获取SIP能力失败"),
    NET_ERROR_GET_WIFI_AP_CFG(371, "获取WIFI ap配置失败"),
    NET_ERROR_SET_WIFI_AP_CFG(372, "设置WIFI ap配置失败"),
    NET_ERROR_GET_DECODE_POLICY(373, "获取解码策略配置失败"),
    NET_ERROR_SET_DECODE_POLICY(374, "设置解码策略配置失败"),
    NET_ERROR_TALK_REJECT(375, "拒绝对讲"),
    NET_ERROR_TALK_OPENED(376, "对讲被其他客户端打开"),
    NET_ERROR_TALK_RESOURCE_CONFLICIT(377, "资源冲突"),
    NET_ERROR_TALK_UNSUPPORTED_ENCODE(378, "不支持的语音编码格式"),
    NET_ERROR_TALK_RIGHTLESS(379, "无权限"),
    NET_ERROR_TALK_FAILED(380, "请求对讲失败"),
    NET_ERROR_GET_MACHINE_CFG(381, "获取机器相关配置失败"),
    NET_ERROR_SET_MACHINE_CFG(382, "设置机器相关配置失败"),
    NET_ERROR_GET_DATA_FAILED(383, "设备无法获取当前请求数据"),
    NET_ERROR_MAC_VALIDATE_FAILED(384, "MAC地址验证失败"),
    NET_ERROR_GET_INSTANCE(385, "获取服务器实例失败"),
    NET_ERROR_JSON_REQUEST(386, "生成的json字符串错误"),
    NET_ERROR_JSON_RESPONSE(387, "响应的json字符串错误"),
    NET_ERROR_VERSION_HIGHER(388, "协议版本低于当前使用的版本"),
    NET_SPARE_NO_CAPACITY(389, "设备操作失败, 容量不足"),
    NET_ERROR_SOURCE_IN_USE(390, "显示源被其他输出占用"),
    NET_ERROR_REAVE(391, "高级用户抢占低级用户资源"),
    NET_ERROR_NETFORBID(392, "禁止入网"),
    NET_ERROR_GETCFG_MACFILTER(393, "获取MAC过滤配置失败"),
    NET_ERROR_SETCFG_MACFILTER(394, "设置MAC过滤配置失败"),
    NET_ERROR_GETCFG_IPMACFILTER(395, "获取IP/MAC过滤配置失败"),
    NET_ERROR_SETCFG_IPMACFILTER(396, "设置IP/MAC过滤配置失败"),
    NET_ERROR_OPERATION_OVERTIME(397, "当前操作超时"),
    NET_ERROR_SENIOR_VALIDATE_FAILED(398, "高级校验失败"),
    NET_ERROR_DEVICE_ID_NOT_EXIST(399, "设备ID不存在"),
    NET_ERROR_UNSUPPORTED(400, "不支持当前操作"),
    NET_ERROR_PROXY_DLLLOAD(401, "代理库加载失败"),
    NET_ERROR_PROXY_ILLEGAL_PARAM(402, "代理用户参数不合法"),
    NET_ERROR_PROXY_INVALID_HANDLE(403, "代理句柄无效"),
    NET_ERROR_PROXY_LOGIN_DEVICE_ERROR(404, "代理登入前端设备失败"),
    NET_ERROR_PROXY_START_SERVER_ERROR(405, "启动代理服务失败"),
    NET_ERROR_SPEAK_FAILED(406, "请求喊话失败"),
    NET_ERROR_NOT_SUPPORT_F6(407, "设备不支持此F6接口调用"),
    NET_ERROR_CD_UNREADY(408, "光盘未就绪"),
    NET_ERROR_DIR_NOT_EXIST(409, "目录不存在"),
    NET_ERROR_UNSUPPORTED_SPLIT_MODE(410, "设备不支持的分割模式"),
    NET_ERROR_OPEN_WND_PARAM(411, "开窗参数不合法"),
    NET_ERROR_LIMITED_WND_COUNT(412, "开窗数量超过限制"),
    NET_ERROR_UNMATCHED_REQUEST(413, "请求命令与当前模式不匹配"),

    NET_RENDER_ENABLELARGEPICADJUSTMENT_ERROR(414, "Render库启用高清图像内部调整策略出错"),
    NET_ERROR_UPGRADE_FAILED(415, "设备升级失败"),
    NET_ERROR_NO_TARGET_DEVICE(416, "找不到目标设备"),
    NET_ERROR_NO_VERIFY_DEVICE(417, "找不到验证设备"),
    NET_ERROR_CASCADE_RIGHTLESS(418, "无级联权限"),
    NET_ERROR_LOW_PRIORITY(419, "低优先级"),
    NET_ERROR_REMOTE_REQUEST_TIMEOUT(420, "远程设备请求超时"),
    NET_ERROR_LIMITED_INPUT_SOURCE(421, "输入源超出最大路数限制"),
    NET_ERROR_SET_LOG_PRINT_INFO(422, "设置日志打印失败"),
    NET_ERROR_PARAM_DWSIZE_ERROR(423, "入参的dwsize字段出错"),
    NET_ERROR_LIMITED_MONITORWALL_COUNT(424, "电视墙数量超过上限"),
    NET_ERROR_PART_PROCESS_FAILED(425, "部分过程执行失败"),
    NET_ERROR_TARGET_NOT_SUPPORT(426, "该功能不支持转发"),
    NET_ERROR_VISITE_FILE(510, "访问文件失败"),
    NET_ERROR_DEVICE_STATUS_BUSY(511, "设备忙"),
    NET_USER_PWD_NOT_AUTHORIZED(512, "修改密码无权限"),
    NET_USER_PWD_NOT_STRONG(513, "密码强度不够"),
    NET_ERROR_NO_SUCH_CONFIG(514, "没有对应的配置"),
    NET_ERROR_AUDIO_RECORD_FAILED(515, "录音失败"),
    NET_ERROR_SEND_DATA_FAILED(516, "数据发送失败"),
    NET_ERROR_OBSOLESCENT_INTERFACE(517, "废弃接口"),
    NET_ERROR_INSUFFICIENT_INTERAL_BUF(518, "内部缓冲不足"),
    NET_ERROR_NEED_ENCRYPTION_PASSWORD(519, "修改设备ip时,需要校验密码"),
    NET_ERROR_NOSUPPORT_RECORD(520, "设备不支持此记录集"),
    NET_ERROR_SERIALIZE_ERROR(1010, "数据序列化错误"),
    NET_ERROR_DESERIALIZE_ERROR(1011, "数据反序列化错误"),
    NET_ERROR_LOWRATEWPAN_ID_EXISTED(1012, "该无线ID已存在"),
    NET_ERROR_LOWRATEWPAN_ID_LIMIT(1013, "无线ID数量已超限"),
    NET_ERROR_LOWRATEWPAN_ID_ABNORMAL(1014, "无线异常添加"),
    NET_ERROR_ENCRYPT(1015, "加密数据失败"),
    NET_ERROR_PWD_ILLEGAL(1016, "新密码不合规范"),
    NET_ERROR_DEVICE_ALREADY_INIT(1017, "设备已经初始化"),
    NET_ERROR_SECURITY_CODE(1018, "安全码错误"),
    NET_ERROR_SECURITY_CODE_TIMEOUT(1019, "安全码超出有效期"),
    NET_ERROR_GET_PWD_SPECI(1020, "获取密码规范失败"),
    NET_ERROR_NO_AUTHORITY_OF_OPERATION(1021, "无权限进行该操作"),
    NET_ERROR_DECRYPT(1022, "解密数据失败"),
    NET_ERROR_2D_CODE(1023, "2D code校验失败"),
    NET_ERROR_INVALID_REQUEST(1024, "非法的RPC请求"),
    NET_ERROR_PWD_RESET_DISABLE(1025, "密码重置功能已关闭"),
    NET_ERROR_PLAY_PRIVATE_DATA(1026, "显示私有数据，比如规则框等失败"),
    NET_ERROR_ROBOT_OPERATE_FAILED(1027, "机器人操作失败"),
    NET_ERROR_PHOTOSIZE_EXCEEDSLIMIT(1028, "图片大小超限"),
    NET_ERROR_USERID_INVALID(1029, "用户ID不存在"),
    NET_ERROR_EXTRACTFEATURE_FAILED(1030, "照片特征值提取失败"),
    NET_ERROR_PHOTO_EXIST(1031, "照片已存在"),
    NET_ERROR_PHOTO_OVERFLOW(1032, "照片数量超过上限"),
    NET_ERROR_CHANNEL_ALREADY_OPENED(1033, "通道已经打开"),
    NET_ERROR_CREATE_SOCKET(1034, "创建套接字失败"),
    NET_ERROR_CHANNEL_NUM(1035, "通道号错误"),
    NET_ERROR_PHOTO_FORMAT(1036, "图片格式错误"),
    /**
     * 内部错误(比如：相关硬件问题，获取公钥失败，内部接口调用失败，写文件失败等等)
     */
    NET_ERROR_DIGITAL_CERTIFICATE_INTERNAL_ERROR(1037, "内部错误"),
    /**
     * 获取设备ID失败
     */
    NET_ERROR_DIGITAL_CERTIFICATE_GET_ID_FAILED(1038, "获取设备ID失败"),
    NET_ERROR_DIGITAL_CERTIFICATE_IMPORT_ILLEGAL(1039, "证书文件非法(格式不支持或者不是证书文件)"),
    NET_ERROR_DIGITAL_CERTIFICATE_SN_ERROR(1040, "证书sn重复或错误或不规范"),
    /**
     * (本地设备证书与系统中的不匹配devid_cryptoID,或者对端的不符合规则(devid_crytoID))
     */
    NET_ERROR_DIGITAL_CERTIFICATE_COMMON_NAME_ILLEGAL(1041, "证书commonName非法"),
    NET_ERROR_DIGITAL_CERTIFICATE_NO_ROOT_CERT(1042, "根证书未导入或不存在"),
    NET_ERROR_DIGITAL_CERTIFICATE_CERT_REVOKED(1043, "证书被吊销"),
    NET_ERROR_DIGITAL_CERTIFICATE_CERT_INVALID(1044, "证书不可用或未生效或已过期"),
    NET_ERROR_DIGITAL_CERTIFICATE_CERT_ERROR_SIGN(1045, "证书签名不匹配"),
    NET_ERROR_DIGITAL_CERTIFICATE_COUNTS_UPPER_LIMIT(1046, "超出证书导入上限"),
    NET_ERROR_DIGITAL_CERTIFICATE_CERT_NO_EXIST(1047, "证书文件不存在(导出证书或者获取对应证书的公钥)"),
    NET_ERROR_DEFULAT_SEARCH_PORT(1048, "默认搜索端口无法使用(5050,37810)"),
    NET_ERROR_FACE_RECOGNITION_SERVER_MULTI_APPEND_STOUP(1049, "批量添加人脸停止"),
    NET_ERROR_FACE_RECOGNITION_SERVER_MULTI_APPEND_ERROR(1050, "批量添加人脸失败"),
    NET_ERROR_FACE_RECOGNITION_SERVER_GROUP_ID_EXCEED(1051, "组ID超过最大值"),
    NET_ERROR_FACE_RECOGNITION_SERVER_GROUP_ID_NOT_IN_REGISTER_GROUP(1052, "组ID不存在或为空"),
    NET_ERROR_FACE_RECOGNITION_SERVER_PICTURE_NOT_FOUND(1053, "无图片数据"),
    NET_ERROR_FACE_RECOGNITION_SERVER_GENERATE_GROUP_ID_FAILED(1054, "生成组ID超出范围"),
    NET_ERROR_FACE_RECOGNITION_SERVER_SET_CONFIG_FAILED(1055, "设置配置失败"),
    NET_ERROR_FACE_RECOGNITION_SERVER_FILE_OPEN_FAILED(1056, "图片文件打开失败"),
    NET_ERROR_FACE_RECOGNITION_SERVER_FILE_READ_FAILED(1057, "图片文件读取失败"),
    NET_ERROR_FACE_RECOGNITION_SERVER_FILE_WRITE_FAILED(1058, "图片文件写入失败"),
    NET_ERROR_FACE_RECOGNITION_SERVER_PICTURE_DPI_ERROR(1059, "图片分辨率异常"),
    NET_ERROR_FACE_RECOGNITION_SERVER_PICTURE_PX_ERROR(1060, "图片像素异常"),
    NET_ERROR_FACE_RECOGNITION_SERVER_PICTURE_SIZE_ERROR(1061, "图片大小不对"),
    NET_ERROR_FACE_RECOGNITION_SERVER_DATA_BASE_ERROR(1062, "数据库操作失败"),
    NET_ERROR_FACE_RECOGNITION_SERVER_FACE_MAX_NUM(1063, "人员数量超过限制"),
    NET_ERROR_FACE_RECOGNITION_SERVER_BIRTH_DAY_FORMAT_ERROR(1064, "生日日期格式错误"),
    NET_ERROR_FACE_RECOGNITION_SERVER_UID_ERROR(1065, "人员UID不存在或为空"),
    NET_ERROR_FACE_RECOGNITION_SERVER_TOKEN_ERROR(1066, "令牌不存在或为空"),
    NET_ERROR_FACE_RECOGNITION_SERVER_BEGIN_NUM_OVER_RUN(1067, "查询起始数大于总数"),
    NET_ERROR_FACE_RECOGNITION_SERVER_ABSTRACT_NUM_ZERO(1068, "需手动建模人数为0"),
    NET_ERROR_FACE_RECOGNITION_SERVER_ABSTRACT_INIT_ERROR(1069, "建模分析器启动失败"),
    NET_ERROR_FACE_RECOGNITION_SERVER_AUTO_ABSTRACT_STATE(1070, "设备正在自动建模"),
    NET_ERROR_FACE_RECOGNITION_SERVER_ABSTRACT_STATE(1071, "设备正在手动建模"),
    NET_ERROR_FACE_RECOGNITION_SERVER_IM_EX_STATE(1072, "设备正在导入导出"),
    NET_ERROR_FACE_RECOGNITION_SERVER_PIC_WRITE_FAILED(1073, "图片写入失败"),
    NET_ERROR_FACE_RECOGNITION_SERVER_GROUP_SPACE_EXCEED(1074, "超出人脸库空间大小限制"),
    NET_ERROR_FACE_RECOGNITION_SERVER_GROUP_PIC_COUNT_EXCEED(1075, "超出人脸库图片数量限制"),
    NET_ERROR_FACE_RECOGNITION_SERVER_GROUP_NOT_FOUND(1076, "人脸库不存在"),
    NET_ERROR_FACE_RECOGNITION_SERVER_FIND_RECORDS_ERROR(1077, "查询原人脸库数据结果无效"),
    NET_ERROR_FACE_RECOGNITION_SERVER_DELETE_PERSON_ERROR(1078, "删除原人脸库数据失败"),
    NET_ERROR_FACE_RECOGNITION_SERVER_DELETE_GROUP_ERROR(1079, "删除人脸库失败"),
    NET_ERROR_FACE_RECOGNITION_SERVER_NAME_FORMAT_ERROR(1080, "命名格式错误"),
    NET_ERROR_FACE_RECOGNITION_SERVER_FILEPATH_NOT_SET(1081, "图片保存路径未设置"),
    NET_ERROR_DEVICE_PARSE_PROTOCOL(1079, "设备解析协议错误"),
    NET_ERROR_DEVICE_INVALID_REQUEST(1080, "设备返回无效请求"),
    NET_ERROR_DEVICE_INTERNAL_ERROR(1081, "设备内部错误"),
    NET_ERROR_DEVICE_REQUEST_TIMEOUT(1082, "设备内部请求超时"),
    NET_ERROR_DEVICE_KEEPALIVE_FAIL(1083, "设备保活失败"),
    NET_ERROR_DEVICE_NETWORK_ERROR(1084, "设备网络错误"),
    NET_ERROR_DEVICE_UNKNOWN_ERROR(1085, "设备内部未知错误"),
    NET_ERROR_DEVICE_COM_INTERFACE_NOTFOUND(1086, "设备组件接口没有找到"),
    NET_ERROR_DEVICE_COM_IMPLEMENT_NOTFOUND(1087, "设备组件实现没有找到"),
    NET_ERROR_DEVICE_COM_NOTFOUND(1088, "设备接入组件没有找到"),
    NET_ERROR_DEVICE_COM_INSTANCE_NOTEXIST(1089, "设备接入组件实例不存在"),
    NET_ERROR_DEVICE_CREATE_COM_FAIL(1090, "设备组件工厂创建组件失败"),
    NET_ERROR_DEVICE_GET_COM_FAIL(1091, "设备组件工厂获取组件实例失败"),
    NET_ERROR_DEVICE_BAD_REQUEST(1092, "设备业务请求不被接受"),
    NET_ERROR_DEVICE_REQUEST_IN_PROGRESS(1093, "设备已经在处理请求，不接受重复请求"),
    NET_ERROR_DEVICE_LIMITED_RESOURCE(1094, "设备资源不足"),
    NET_ERROR_DEVICE_BUSINESS_TIMEOUT(1095, "设备业务超时"),
    NET_ERROR_DEVICE_TOO_MANY_REQUESTS(1096, "设备接收过多请求"),
    NET_ERROR_DEVICE_NOT_ALREADY(1097, "设备未准备就绪，不接受业务请求"),
    NET_ERROR_DEVICE_SEARCHRECORD_TIMEOUT(1098, "设备录像查询超时"),
    NET_ERROR_DEVICE_SEARCHTIME_INVALID(1099, "设备录像查询时间无效"),
    NET_ERROR_DEVICE_SSID_INVALID(1100, "设备校验SSID无效"),
    NET_ERROR_DEVICE_CHANNEL_STREAMTYPE_ERROR(1101, "设备校验通道号或码流类型无效"),
    NET_ERROR_DEVICE_STREAM_PACKINGFORMAT_UNSUPPORT(1102, "设备不支持该码流打包格式"),
    NET_ERROR_DEVICE_AUDIO_ENCODINGFORMAT_UNSUPPORT(1103, "设备不支持该语音编码格式"),
    NET_ERROR_SECURITY_ERROR_SUPPORT_GUI(1104, " 校验请求安全码失败,可使用本地GUI方式重置密码"),
    NET_ERROR_SECURITY_ERROR_SUPPORT_MULT(1105, "校验请求安全码失败,可使用大华渠道APP、configtool工具重置密码"),
    NET_ERROR_SECURITY_ERROR_SUPPORT_UNIQUE(1106, "校验请求安全码失败,可登陆Web页面重置密码"),
    NET_ERROR_STREAMCONVERTOR_DEFECT(1107, "转码库缺失"),


    NET_ERROR_SECURITY_GENERATE_SAFE_CODE(1108, "调用大华加密库产生安全码失败"),
    NET_ERROR_SECURITY_GET_CONTACT(1109, "获取联系方式失败"),
    NET_ERROR_SECURITY_GET_QRCODE(1110, "获取重置密码的二维码信息失败"),
    NET_ERROR_SECURITY_CANNOT_RESET(1111, "设备未初始化,无法重置"),
    NET_ERROR_SECURITY_NOT_SUPPORT_CONTACT_MODE(1112, "不支持设置该种联系方式,如只支持设置手机号，却请求设置邮箱"),
    NET_ERROR_SECURITY_RESPONSE_TIMEOUT(1113, "对端响应超时"),
    NET_ERROR_SECURITY_AUTHCODE_FORBIDDEN(1114, "尝试校验AuthCode次数过多，禁止校验"),
    NET_ERROR_TRANCODE_LOGIN_REMOTE_DEV(1115, "(虚拟转码)登陆远程设备失败"),
    NET_ERROR_TRANCODE_NOFREE_CHANNEL(1116, "(虚拟转码)没有可用的通道资源"),
    NET_ERROR_VK_INFO_DECRYPT_FAILED(1117, "VK信息解密失败"),
    NET_ERROR_VK_INFO_DESERIALIZE_FAILED(1118, "VK信息解析失败"),
    NET_ERROR_GDPR_ABILITY_NOT_ENABLE(1119, "SDK GDPR功能未使能"),

    /*门禁快速导入及复核错误码 start*/
    NET_ERROR_FAST_CHECK_NO_AUTH(1120, "门禁快速复核:无权限"),
    NET_ERROR_FAST_CHECK_NO_FILE(1121, "门禁快速复核:文件不存在"),
    NET_ERROR_FAST_CHECK_FILE_FAIL(1122, "门禁快速复核:文件准备失败"),
    NET_ERROR_FAST_CHECK_BUSY(1123, "门禁快速复核:系统忙"),
    NET_ERROR_FAST_CHECK_NO_PASSWORD(1124, "门禁快速复核:未定义密码, 不允许导出"),
    NET_ERROR_IMPORT_ACCESS_SEND_FAILD(1125, "门禁快速导入:发送门禁数据失败"),
    NET_ERROR_IMPORT_ACCESS_BUSY(1126, "门禁快速导入:系统忙, 已经有导入任务"),
    NET_ERROR_IMPORT_ACCESS_DATAERROR(1127, "门禁快速导入:数据包检验失败"),
    NET_ERROR_IMPORT_ACCESS_DATAINVALID(1128, "门禁快速导入:数据包非法"),
    NET_ERROR_IMPORT_ACCESS_SYNC_FALID(1129, "门禁快速导入:同步失败,数据库无法生成"),
    NET_ERROR_IMPORT_ACCESS_DBFULL(1130, "门禁快速导入:数据库已满, 无法导入"),
    NET_ERROR_IMPORT_ACCESS_SDFULL(1131, "门禁快速导入:存储空间已满, 无法导入"),
    NET_ERROR_IMPORT_ACCESS_CIPHER_ERROR(1132, "门禁快速导入:导入压缩包密码不对"),
    /*门禁快速导入及复核错误码 end*/

    NET_ERROR_INVALID_PARAM(1133, "参数无效"),
    NET_ERROR_INVALID_PASSWORD(1134, "密码无效"),
    NET_ERROR_INVALID_FINGERPRINT(1135, "信息无效"),
    NET_ERROR_INVALID_FACE(1136, "人脸无效"),
    NET_ERROR_INVALID_CARD(1137, "卡无效"),
    NET_ERROR_INVALID_USER(1138, "用户无效"),
    NET_ERROR_GET_SUBSERVICE(1139, "能力集子服务获取失败"),
    NET_ERROR_GET_METHOD(1140, "获取组件的方法集失败"),
    NET_ERROR_GET_SUBCAPS(1141, "获取资源实体能力集失败"),
    NET_ERROR_UPTO_INSERT_LIMIT(1142, "已达插入上限"),
    NET_ERROR_UPTO_MAX_INSERT_RATE(1143, "已达最大插入速度"),
    NET_ERROR_ERASE_FINGERPRINT(1144, "清除信息数据失败"),
    NET_ERROR_ERASE_FACE(1145, "清除人脸数据失败"),
    NET_ERROR_ERASE_CARD(1146, "清除卡数据失败"),
    NET_ERROR_NO_RECORD(1147, "没有记录"),
    NET_ERROR_NOMORE_RECORDS(1148, "查找到最后，没有更多记录"),
    NET_ERROR_RECORD_ALREADY_EXISTS(1149, "下发卡或信息时，数据重复"),
    NET_ERROR_EXCEED_MAX_FINGERPRINT_PERUSER(1150, "超过个人最大信息记录数"),
    NET_ERROR_EXCEED_MAX_CARD_PERUSER(1151, "超过个人最大卡片记录数"),
    NET_ERROR_EXCEED_ADMINISTRATOR_LIMIT(1152, "超过门禁管理员个数限制"),

    NET_LOGIN_ERROR_DEVICE_NOT_SUPPORT_HIGHLEVEL_SECURITY_LOGIN(1153, "设备不支持高安全等级登录"),
    NET_LOGIN_ERROR_DEVICE_ONLY_SUPPORT_HIGHLEVEL_SECURITY_LOGIN(1154, "设备只支持高安全等级登录"),

    NET_ERROR_VIDEO_CHANNEL_OFFLINE(1155, "此视频通道处于离线，拉流失败"),
    NET_ERROR_USERID_FORMAT_INCORRECT(1156, "用户编号不规范"),
    NET_ERROR_CANNOT_FIND_CHANNEL_RELATE_TO_SN(1157, "找不到该SN对应的通道"),
    NET_ERROR_TASK_QUEUE_OF_CHANNEL_IS_FULL(1158, "该通道的任务队列满"),
    NET_ERROR_APPLY_USER_INFO_BLOCK_FAIL(1159, "申请不到新的用户信息(权限)块"),
    NET_ERROR_EXCEED_MAX_PASSWD_PERUSER(1160, "用户密码数量超过限制"),
    NET_ERROR_PARSE_PROTOCOL(1161, "设备内部异常引起协议解析错误"),
    NET_ERROR_CARD_NUM_EXIST(1162, "卡号已存在"),
    NET_ERROR_FINGERPRINT_EXIST(1163, "信息已存在"),

    NET_ERROR_OPEN_PLAYGROUP_FAIL(1164, "打开播放组失败"),
    NET_ERROR_ALREADY_IN_PLAYGROUP(1165, "已位于播放组中"),
    NET_ERROR_QUERY_PLAYGROUP_TIME_FAIL(1166, "查询播放组时间失败"),
    NET_ERROR_SET_PLAYGROUP_BASECHANNEL_FAIL(1167, "设置播放组基准通道失败"),
    NET_ERROR_SET_PLAYGROUP_DIRECTION_FAIL(1168, "设置播放组方向失败"),
    NET_ERROR_SET_PLAYGROUP_SPEED_FAIL(1169, "设置播放组速度失败"),
    NET_ERROR_ADD_PLAYGROUP_FAIL(1170, "加入播放组失败"),

    NET_ERROR_EXPORT_AOL_LOGFILE_NO_AUTH(1171, " 导出AOL日志:无权限"),
    NET_ERROR_EXPORT_AOL_LOGFILE_NO_FILE(1172, "导出AOL日志:文件不存在"),
    NET_ERROR_EXPORT_AOL_LOGFILE_FILE_FAIL(1173, "导出AOL日志:文件准备失败"),
    NET_ERROR_EXPORT_AOL_LOGFILE_BUSY(1174, "导出AOL日志:系统忙"),

    /**
     * 设备上APP安装相关错误码
     */
    NET_ERROR_EMPTY_LICENSE(1175, "License为空"),
    NET_ERROR_UNSUPPORTED_MODE(1176, "不支持该模式"),
    NET_ERROR_URL_APP_NOT_MATCH(1177, "URL与APP不匹配"),
    NET_ERROR_READ_INFO_FAILED(1178, "读取信息失败"),
    NET_ERROR_WRITE_FAILED(1179, "写入失败"),
    NET_ERROR_NO_SUCH_APP(1180, "未找到APP"),
    NET_ERROR_VERIFIF_FAILED(1181, "校验失败"),
    NET_ERROR_LICENSE_OUT_DATE(1182, "License已过期"),

    NET_ERROR_UPGRADE_PROGRAM_TOO_OLD(1183, "升级程序版本过低"),
    NET_ERROR_SECURE_TRANSMIT_BEEN_CUT(1184, "加密传输被裁剪"),
    NET_ERROR_DEVICE_NOT_SUPPORT_SECURE_TRANSMIT(1185, "设备不支持安全传输"),

    NET_ERROR_EXTRA_STREAM_LOGIN_FAIL_CAUSE_BY_MAIN_STREAM(1186, "主码流成功的情况下，辅码流登录失败"),
    NET_ERROR_EXTRA_STREAM_CLOSED_BY_REMOTE_DEVICE(1187, "辅码流被前端关闭"),

    /*人脸库导入导出错误码 start*/
    NET_ERROR_IMPORT_FACEDB_SEND_FAILD(1188, "人脸库导入:发送人脸库数据失败"),
    NET_ERROR_IMPORT_FACEDB_BUSY(1189, "人脸库导入:系统忙, 已经有导入任务"),
    NET_ERROR_IMPORT_FACEDB_DATAERROR(1190, "人脸库导入:数据包检验失败"),
    NET_ERROR_IMPORT_FACEDB_DATAINVALID(1191, "人脸库导入:数据包非法"),
    NET_ERROR_IMPORT_FACEDB_UPGRADE_FAILD(1192, "人脸库导入:上传失败"),
    NET_ERROR_IMPORT_FACEDB_NO_AUTHORITY(1193, "人脸库导入:用户无权限"),
    NET_ERROR_IMPORT_FACEDB_ABNORMAL_FILE(1194, " 人脸库导入:文件格式异常"),
    NET_ERROR_IMPORT_FACEDB_SYNC_FALID(1195, "人脸库导入:同步失败,数据库无法生成"),
    NET_ERROR_IMPORT_FACEDB_DBFULL(1196, "人脸库导入:数据库已满, 无法导入"),
    NET_ERROR_IMPORT_FACEDB_SDFULL(1197, "人脸库导入:存储空间已满, 无法导入"),
    NET_ERROR_IMPORT_FACEDB_CIPHER_ERROR(1198, "人脸库导入:导入压缩包密码不对"),

    NET_ERROR_EXPORT_FACEDB_NO_AUTH(1199, "人脸库导出:无权限"),
    NET_ERROR_EXPORT_FACEDB_NO_FILE(1200, "人脸库导出:文件不存在"),
    NET_ERROR_EXPORT_FACEDB_FILE_FAIL(1201, "人脸库导出:文件准备失败"),
    NET_ERROR_EXPORT_FACEDB_BUSY(1202, "人脸库导出:系统忙"),
    NET_ERROR_EXPORT_FACEDB_NO_PASSWORD(1203, "人脸库导出:未定义密码, 不允许导出"),
    /*人脸库导入导出错误码 end*/

    /* 人脸图片操作错误码 范围 _EC(1300) ~ _EC(1400) */
    NET_ERROR_FACEMANAGER_NO_FACE_DETECTED(1300, "图片中检测到0个人脸目标"),
    NET_ERROR_FACEMANAGER_MULTI_FACE_DETECTED(1301, "图片中检测到多个人脸，无法返回特征"),
    NET_ERROR_FACEMANAGER_PICTURE_DECODING_ERROR(1302, "图片解码错误"),
    NET_ERROR_FACEMANAGER_LOW_PICTURE_QUALITY(1303, "图片质量太低"),
    /**
     * 结果不推荐使用,比如：对外国人，特征提取成功，但算法支持不好，容易造成误识别
     */
    NET_ERROR_FACEMANAGER_NOT_RECOMMENDED(1304, "结果不推荐使用"),
    NET_ERROR_FACEMANAGER_FACE_FEATURE_ALREADY_EXIST(1305, "人脸特征已存在"),
    NET_ERROR_FACEMANAGER_FACE_ANGLE_OVER_THRESHOLDS(1307, "人脸角度超过配置阈值"),
    NET_ERROR_FACEMANAGER_FACE_RADIO_EXCEEDS_RANGE(1308, "人脸占比超出范围，算法建议占比:不要超过2/3,不要小于1/3"),
    NET_ERROR_FACEMANAGER_FACE_OVER_EXPOSED(1309, "人脸过爆"),
    NET_ERROR_FACEMANAGER_FACE_UNDER_EXPOSED(1310, "人脸欠爆"),
    NET_ERROR_FACEMANAGER_BRIGHTNESS_IMBALANCE(1311, "人脸亮度不均衡 ,用于判断阴阳脸"),
    NET_ERROR_FACEMANAGER_FACE_LOWER_CONFIDENCE(1312, "人脸的置信度低"),
    NET_ERROR_FACEMANAGER_FACE_LOW_ALIGN(1313, "人脸对齐分数低"),
    NET_ERROR_FACEMANAGER_FRAGMENTARY_FACE_DETECTED(1314, "人脸存在遮挡、残缺不全"),
    NET_ERROR_FACEMANAGER_PUPIL_DISTANCE_NOT_ENOUGH(1315, "人脸瞳距小于阈值"),
    NET_ERROR_FACEMANAGER_FACE_DATA_DOWNLOAD_FAILED(1316, " 人脸数据下载失败");

    private final int code;
    private final String error;

    private ENUMERROR(int code, String error) {
        this.code = code;
        this.error = error;
    }

    public int getCode() {
        return code;
    }

    public String getError() {
        return error;
    }

    public static ENUMERROR getENUMError() {
        int code = NetSDKLib.NETSDK_INSTANCE.CLIENT_GetLastError() & 0x7fffffff;
        for (ENUMERROR error : ENUMERROR.values()) {
            if (error.getCode() == code) {
                return error;
            }
        }
        return NET_UNDEFIND;
    }

    /**
     * 错误信息
     */
    public static String getErrorMessage() {
        return getENUMError().getError();
    }

    /**
     * 错误码
     */
    public static int getErrorCode() {
        return getENUMError().getCode();
    }

    /**
     * 错误码+错误信息
     */
    public static String getFullError() {
        ENUMERROR error = getENUMError();
        return error.getCode() + "," + error.getError();
    }
}
