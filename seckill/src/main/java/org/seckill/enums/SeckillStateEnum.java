package org.seckill.enums;

/**
 * 枚举表示常量数据字典
 * 
 * @author chuanfu
 *
 */
public enum SeckillStateEnum {
	SUCCESS(1,"秒杀成功"),
	END(0,"秒杀结束"),
	REPEAT_KILL(-1,"重复秒杀"),
	INNER_ERROR(-2,"系统异常"),
	DATA_REWRITE(-3,"数据篡改");
	
	
	private int state;
	private String ststeInfo;

	private SeckillStateEnum(int state, String ststeInfo) {
		this.state = state;
		this.ststeInfo = ststeInfo;
	}

	public int getState() {
		return state;
	}

	public String getStsteInfo() {
		return ststeInfo;
	}

	public static SeckillStateEnum stateOf(int index) {
		for (SeckillStateEnum state : values()) {
			if (state.getState() == index) {
				return state;
			}
		}
		return null;
	}

}