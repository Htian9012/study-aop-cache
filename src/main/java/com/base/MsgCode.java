package com.base;


/**
 * 返回状态码
 * @author lizehao
 * @version 创建时间：2019年04月11日 下午03:06:50
 *
 */
public class MsgCode {

	private String msgCode;
	private String message;


	/**
	通用的错误码
	 */
	public static MsgCode SUCCESS = new MsgCode("100", "请求成功");
	public static MsgCode SYSTEM_FAIL = new MsgCode("200", "系统错误,请稍后重试");
	public static MsgCode BAD_REQUEST = new MsgCode("300", "参数有误，请重新请求");
	public static MsgCode SYSTEM_ERROR= new MsgCode("500", "系统繁忙,请稍后重试");
	public static MsgCode UNAUTHORIZED = new MsgCode("401", "未登录");

	private MsgCode( ) {
	}

	public MsgCode(String msgCode, String message) {
		this.msgCode = msgCode;
		this.message = message;
	}

	public String getMsgCode() {
		return msgCode;
	}

	public void setMsgCode(String msgCode) {
		this.msgCode = msgCode;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String code() {
		return msgCode;
	}

	@Override
	public String toString() {
		return "MsgCode{" +
				"msgCode='" + msgCode + '\'' +
				", message='" + message + '\'' +
				'}';
	}
}
