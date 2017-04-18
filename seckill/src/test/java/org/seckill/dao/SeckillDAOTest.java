package org.seckill.dao;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.seckill.entity.Seckill;
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

public class SeckillDAOTest {
//注入DAO实现类依赖
	@Resource
	private SeckillDAO seckillDao;
	
	@Test
	public void testReduceNumber() {
		Date killTime = new Date();
		int reduceCount = seckillDao.reduceNumber(1000L, killTime);
		System.out.println(reduceCount);
	}

	@Test
	public void testQueryById() {
		long id =1000;
		Seckill seckill = seckillDao.queryById(id);
		System.out.println(seckill);
		/**
		 *Seckill [seckillId=1000, name=iphone6, number=100, 
		 *startTime=Mon Apr 17 00:00:00 CST 2017, 
		 *endTime=Mon Apr 17 00:00:00 CST 2017, 
		 *createTime=Mon Apr 17 18:46:01 CST 2017]
		 */
	}
	@Test
	public void testQueryAll() {
		List<Seckill> seckillList = seckillDao.queryAll(0, 100);
		for(Seckill seckill:seckillList){
			System.out.println(seckill);
		}
	}

}
