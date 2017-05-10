package org.seckill.exception;
/**
 * 秒杀关闭异常
 * @author chuanfu
 *
 */
public class SeckillCloseException extends SeckillException{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6290425115435104747L;

	public SeckillCloseException(String message, Throwable cause) {
		super(message, cause);
	}

	public SeckillCloseException(String message) {
		super(message);
	}

	
}
