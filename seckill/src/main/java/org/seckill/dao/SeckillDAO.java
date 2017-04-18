package org.seckill.dao;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.seckill.entity.Seckill;

public interface SeckillDAO {
/**
 * 减库存
 * @param seckillId
 * @param killTime
 * @return
 * 更新数量
 */
	int reduceNumber(@Param("seckillId") long seckillId,@Param("killTime") Date killTime);
	/**
	 * 根据Id查询
	 * @param seckillId
	 * @return
	 */
	Seckill queryById(long seckillId);
	/**
	 * 分页查询
	 * @param offet
	 * @param limit
	 * @return
	 * Java 不保存形参名称 -->queryAll(arg0,arg1)
	 */
	List<Seckill> queryAll(@Param("offset") int offset,@Param("limit") int limit);
}
