

package com.mybatis.plus.config.exception;

import cn.hutool.core.util.StrUtil;

/**
 *
 * 描述: 自定义异常
 *
 * @author 官萧何
 * @date 2020/8/17 11:18
 * @version V1.0
 */
public class RRException extends RuntimeException {

    private String msg;
    private int code = 500;

    public RRException(String msg) {
		super(msg);
		this.msg = msg;
	}

	public RRException(String msg, Throwable e) {
		super(msg, e);
		this.msg = msg;
	}

	public static RRException MyException(String msg, Object... args){
		String format = StrUtil.format(msg, args);
		return new RRException(format);
	}

	public RRException(String msg, int code) {
		super(msg);
		this.msg = msg;
		this.code = code;
	}

	public RRException(String msg, int code, Throwable e) {
		super(msg, e);
		this.msg = msg;
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}


}
