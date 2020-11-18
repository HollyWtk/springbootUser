package com.yhh.core.utils;

public class ResMsg {

	public int code;

	public String message;

	public Object data;

	public boolean isSuccess() {
		return code == 0;
	}

	public static ResMsg SUCCESS() {

		ResMsg ret = new ResMsg();

		ret.code = 0;

		ret.message = "操作成功";

		return ret;
	}

	public static ResMsg ERROR_INNOR() {

		ResMsg ret = new ResMsg();

		ret.code = -1;

		ret.message = "系统内部错误 ! 程序猿小哥正在抢修,请稍后再试";

		return ret;

	}

	public static ResMsg ERROR(int error_code, String msg) {

		ResMsg ret = new ResMsg();

		ret.code = error_code;

		ret.message = msg;

		return ret;

	}

	public static ResMsg ERROR_MESSAGE(int error_code, String msg) {

		ResMsg ret = new ResMsg();

		ret.code = error_code;

		ret.message = msg;

		return ret;

	}

	public static ResMsg SUCCESS_RESULT(Object data) {

		ResMsg ret = new ResMsg();

		ret.code = 0;

		ret.message = "操作成功";

		ret.data = data;

		return ret;

	}
	
	public static ResMsg SUCCESS_MESSAGE(String message) {

		ResMsg ret = new ResMsg();

		ret.code = 0;

		ret.message = message;

		
		return ret;

	}
}
