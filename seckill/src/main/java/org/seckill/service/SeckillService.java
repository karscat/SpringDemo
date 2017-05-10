package org.seckill.service;

import java.util.List;

import org.seckill.dto.Exposer;
import org.seckill.dto.SeckillExecution;
import org.seckill.entity.Seckill;
import org.seckill.exception.*;

/**
 * 业务接口 ：站在使用者角度设计接口
 * @author chuanfu
 * 1)方法定义粒度
 * 2)参数
 * 3)返回类型（return/异常）
 */
public interface SeckillService {

	/**
	 * 展示秒杀商品列表
	 * @return
	 */
	List<Seckill> getSeckillList();
	/**
	 * 查询单个秒杀记录
	 * @param seckillId
	 * @return
	 */
	Seckill getById(long seckillId);
	
	/**
	 * 秒杀开启时，输出秒杀接口地址，否则输出系统时间和秒杀时间
	 * @param seckillId
	 */
	Exposer exportSeckillUrl(long seckillId);
	
	/**
	 * 执行秒杀操作,抛出异常
	 * @param seckillId
	 * @param userphone
	 * @param md5
	 */
	SeckillExecution executeSeckill(long seckillId,long userPhone,String md5) 
			throws SeckillException,RepeatKillException,SeckillCloseException;
}






