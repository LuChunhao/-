package cn.falconnect.shopping.provider.web;

public enum ResultCode {

	SUCCESS(0, "成功"),
	NO_MORE_DATA(1, "没有更多数据啦"),
	PARAMETER_ERROR(2, "服务器内部错误"),
	PARAMETER_VALUE_FAILURE(3, "无效的参数"),
	DATA_CHECKCODE_FAILURE(4, "服务已经停用"),
	INVALID_PROTOCOL_VERSION(5, "参数错误"),
	NET_STREAM_ERROR(10, "无效的协议"),
	STREAM_ERROR(12, "数据通信出错"),
	SERVER_ON_ERROR(800, "服务器内部错误"),
	DB_OPERATION_ERROR(900, "数据库错误"),
	PASSWORD_ERROR(10010, "密码错误"),
	ACCOUNT_NOT(10001, "账号不存在"),
	ACCOUNT_EXIST(10002, "账号已经存在"),
	NICKNAME_EXIST(10020,"昵称已存在"),
	PASSWD_ERROR(10010, "密码错误"),
	RE_LOGIN(5000, "签名信息过期,需要重新登录"),
	LOGIN(5001, "登录过时，请先登录"),
	CONTENT_LENGTH_TO_LONG(996, "请求数据长度太长"),
	UNKNOW_SERVER_ERROR(997, "未知的服务器错误"),
	SERVER_MAINTENANCE(998, "服务器维护"),
	NET_ERROR(99999, "网络连接错误，请检查后重试"),
	SERVER_ERROR(99998, "服务器异常，请稍后重试"),
	UNKOWN(10099, "未知错误"), NO_DATA(99997, "没有数据");
	
	public int code;
	public String msg;

	ResultCode(int code, String msg) {
		this.code = code;
		this.msg = msg;
	}

	public static ResultCode getEnum(int code) {
		for (ResultCode resultCode : values()) {
			if (resultCode.code == code) {
				return resultCode;
			}
		}
		return UNKOWN;
	}
}
