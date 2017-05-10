package org.seckill.dao.cache;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.seckill.dao.SeckillDAO;
import org.seckill.entity.Seckill;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
//告诉junit spring配置位置
@ContextConfiguration({"classpath:spring/spring-dao.xml"})

public class RedisDAOTest {

	private Long seckillId = 1001L;
	@Autowired
	private RedisDAO redisDAO;
	@Autowired
	private SeckillDAO seckillDAO;
	@Test
	public void testSeckill() {
		Seckill seckill = redisDAO.getSeckill(seckillId);
		if(seckill==null){
			seckill = seckillDAO.queryById(seckillId);
			if(seckill!=null){
				String result = redisDAO.putSeckill(seckill);
				System.out.println(result);
				seckill = redisDAO.getSeckill(seckillId);
				System.out.println(seckill);
			}			
		}
	}
}









