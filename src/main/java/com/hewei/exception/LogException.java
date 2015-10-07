package com.hewei.exception;
/**
 * 
 * @author hewei
 * 
 * @date 2015/9/17  1:34
 *
 * @version 5.0
 *
 * @desc 
 *
 */
public class LogException extends RuntimeException {

	private String errDesc;


	public LogException(String errDesc) {
		super(errDesc);
		this.errDesc = errDesc;
	}

	public String getErrDesc() {
		return errDesc;
	}
}
