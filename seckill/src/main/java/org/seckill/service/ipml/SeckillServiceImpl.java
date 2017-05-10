package org.seckill.service.ipml;

import java.util.Date;
import java.util.List;

import org.seckill.dao.SeckillDAO;
import org.seckill.dao.SuccessKilledDAO;
import org.seckill.dto.Exposer;
import org.seckill.dto.SeckillExecution;
import org.seckill.entity.Seckill;
import org.seckill.entity.SuccessKilled;
import org.seckill.enums.SeckillStateEnum;
import org.seckill.exception.RepeatKillException;
import org.seckill.exception.SeckillCloseException;
import org.seckill.exception.SeckillException;
import org.seckill.service.SeckillService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;

//@Component 代表所有组件实例 @Service @Dao @Controller
@Service
public class SeckillServiceImpl implements SeckillService {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());
		
	//注入Service依赖
	@Autowired
	private SeckillDAO seckillDAO;
	
	@Autowired
	private SuccessKilledDAO successKilledDAO;

	// md5盐值，用于混淆MD5
	private final String slat = "3333^&*^*asadasdasdasdasdfgh546456";

	@Override
	public List<Seckill> getSeckillList() {
		return seckillDAO.queryAll(0, 4);
	}

	@Override
	public Seckill getById(long seckillId) {
		return seckillDAO.queryById(seckillId);
	}

	@Override
	@Transactional
	/**
	 * 使用注解控制事务方法的优点：
	 * 1、开发团队达成一致约定，明确标注事务方法的编程风格
	 * 2、保证事务方法的执行时间尽可能短，不要穿插其他网络操作RPC/HTTP请求或者剥离到事务方法外部
	 * 3、不是所有方法都需要事务
	 */
	public Exposer exportSeckillUrl(long seckillId) {
		//优化点：缓存优化
		Seckill seckill = seckillDAO.queryById(seckillId);
		
		if (seckill == null) {
			return new Exposer(false, seckillId);
		}
		Date startTime = seckill.getStartTime();
		Date endTime = seckill.getEndTime();
		Date currentTime = new Date();
		if (currentTime.getTime() < startTime.getTime() || currentTime.getTime() > endTime.getTime()) {
			return new Exposer(false, seckillId, currentTime.getTime(), startTime.getTime(), endTime.getTime());
		}
		// 转化特定字符串，不可逆
		String md5 = getMD5(seckillId);
		return new Exposer(true, md5, seckillId);
	}

	@Override
	public SeckillExecution executeSeckill(long seckillId, long userPhone, String md5)
			throws SeckillException, RepeatKillException, SeckillCloseException {
		if (md5 == null || !md5.equals(getMD5(seckillId))) {
			throw new SeckillException("seckill data rewrite");
		}
		// 执行秒杀业务逻辑：减库存，记录购物
		Date currentTime = new Date();

		try {
			// 减库存
			int updateCount = seckillDAO.reduceNumber(seckillId, currentTime);
			if (updateCount <= 0) {
				// 没有更新，秒杀结束
				throw new SeckillCloseException("seckill closed");
			} else {
				// 记录购物
				int insertCount = successKilledDAO.insertSuccessKilled(seckillId, userPhone);
				if (insertCount <= 0) {
					// 重复秒杀
					throw new RepeatKillException("seckill repeated");
				} else {
					SuccessKilled successKilled = successKilledDAO.queryByIdWithSeckill(seckillId, userPhone);
					return new SeckillExecution(seckillId, SeckillStateEnum.SUCCESS,successKilled);
				}
			}
		} catch (SeckillCloseException e) {
			throw e;
		} catch (RepeatKillException e) {
			throw e;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			// 所有编译期异常，转化为运行期异常
			throw new SeckillException("seckill inner error:" + e.getMessage());
		}
	}

	private String getMD5(long seckillId) {
		String base = seckillId + "/" + slat;
		String md5 = DigestUtils.md5DigestAsHex(base.getBytes());
		return md5;
	}
}
