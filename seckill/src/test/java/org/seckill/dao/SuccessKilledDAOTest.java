package org.seckill.dao;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.seckill.entity.SuccessKilled;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * 配置spring-Junit整合，Junit启动时加载ioc容器
 * @author chuanfu
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
//告诉junit spring配置位置
@ContextConfiguration({"classpath:spring/spring-dao.xml"})

public class SuccessKilledDAOTest {

	//注入DAO实现类依赖
		@Resource
		private SuccessKilledDAO successKilledDao;
		
	@Test
	public void testInsertSuccessKilled() {
		
		int seckillCount = successKilledDao.insertSuccessKilled(1000L, 1456789213L);
		System.out.println(seckillCount);
	}

	@Test
	public void testQueryByIdWithSeckill() {
		
		SuccessKilled seckillCount = successKilledDao.queryByIdWithSeckill(1000L,1456789213L);
		System.out.println(seckillCount);
		System.out.println(seckillCount.getSeckill());
	}

}
