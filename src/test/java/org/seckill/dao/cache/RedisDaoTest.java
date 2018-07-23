package org.seckill.dao.cache;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.seckill.dao.StockDao;
import org.seckill.entity.Stock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.*;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:spring/spring-dao.xml"})
public class RedisDaoTest {
    @Autowired
    private RedisDao redisDao;

    @Autowired
    private StockDao stockDao;


    @Test
    public void test() {

        long secillId = 1001;

        Stock seckill = redisDao.getSeckill(secillId);

        if (seckill == null) {
            seckill = stockDao.queryById(secillId);
            if (seckill != null) {
                String result = redisDao.putSeckill(seckill);
                System.out.println(result);
            }
        }
    }
}