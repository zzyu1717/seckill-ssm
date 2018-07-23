package org.seckill.service.impl;

import org.seckill.dao.StockDao;
import org.seckill.dao.SuccessKilledDao;
import org.seckill.dao.cache.RedisDao;
import org.seckill.dto.Exposer;
import org.seckill.dto.SeckillExecution;
import org.seckill.entity.Stock;
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

import java.util.Date;
import java.util.List;

@Service
public class SeckillServiceImpl implements SeckillService {

    @Autowired
    private StockDao stockDao;

    @Autowired
    private RedisDao redisDao;

    @Autowired
    private SuccessKilledDao successKilledDao;

    private String salt = "jdfjfjlrejetjjl&&((^^%&&&&%$$*I())*Fdjfjd";

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public List<Stock> getSeckillList() {

        return stockDao.queryAll(0, 5);
    }

    @Override
    public Stock getById(long stockId) {
        return stockDao.queryById(stockId);
    }

    @Override
    public Exposer exportSeckillUrl(long stockId) {

        Stock stock = redisDao.getSeckill(stockId);

        if (stock == null) {
            stock = stockDao.queryById(stockId);

            if (stock == null) {
                return new Exposer(false, stockId);
            } else {
                // 放入redis缓存
                redisDao.putSeckill(stock);
            }
        }

        long startTime = stock.getStartTime().getTime();
        long endTime = stock.getEndTime().getTime();
        // 系统当前时间
        long nowTime = new Date().getTime();
        if (nowTime < startTime || nowTime > endTime) {
            return new Exposer(false, stockId, nowTime, startTime, endTime);
        }
        String md5 = getMD5(stockId);
        return new Exposer(true, stockId, md5);
    }

    @Override
    @Transactional // 事务注解
    public SeckillExecution executeSeckill(long stockId, long userPhone, String md5) throws SeckillException {
        // 数据被篡改
        if (md5 == null || !md5.equals(getMD5(stockId))) {
            throw new SeckillException(SeckillStateEnum.DATA_REWRITE.getStateInfo());
        }

        /**
         * 执行秒杀逻辑：减库存+记录购买行为
         */
        try {
            // 减库存
            Date now = new Date();
            int updateCount = stockDao.reduceNumber(stockId, now);
            if (updateCount <= 0) {
                // 没有更新到记录，秒杀结束
                throw new SeckillException(SeckillStateEnum.END.getStateInfo());
            } else {
                /**
                 * test 抛出受检异常，查看事务能否正确回滚
                 */
               /* if (1 == 1) {
                    throw new Exception("test");
                }*/

                // 记录购买行为
                int insertCount = successKilledDao.insertSuccessKilled(stockId, userPhone);
                if (insertCount <= 0) {
                    // 重复秒杀
                    throw new RepeatKillException(SeckillStateEnum.REPEAT_KILL.getStateInfo());
                } else {
                    // 秒杀成功
                    SuccessKilled successKilled = successKilledDao.queryByIdWithStock(stockId, userPhone);
                    return new SeckillExecution(stockId, SeckillStateEnum.SUCCESS, successKilled);
                }
            }

        } catch (Exception e) {
            logger.error(e.getMessage(),e);
            // 所有编译期异常，转化为运行期异常
            throw new SeckillException(e.getMessage());
        }
    }

    private String getMD5(long stockId) {
        String base = stockId + "/" + salt;
        return DigestUtils.md5DigestAsHex(base.getBytes());
    }
}
