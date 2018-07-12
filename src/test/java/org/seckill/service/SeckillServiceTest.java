package org.seckill.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.seckill.dto.Exposer;
import org.seckill.dto.SeckillExecution;
import org.seckill.entity.Stock;
import org.seckill.exception.RepeatKillException;
import org.seckill.exception.SeckillCloseException;
import org.seckill.exception.SeckillException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:spring/spring-dao.xml","classpath:spring/spring-service.xml"})
public class SeckillServiceTest {

    @Autowired
    private SeckillService seckillService;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Test
    public void getSeckillList() throws Exception {
        List<Stock> seckillList = seckillService.getSeckillList();
        logger.info("list {}",seckillList);
    }

    @Test
    public void getById() throws Exception {
        Stock stock = seckillService.getById(1000);
        logger.debug("stock {}", stock);
    }

    @Test
    public void testSeckillLogic() throws Exception {
        long stockId = 1000;
               Exposer exposer = seckillService.exportSeckillUrl(stockId);
                logger.info("exposer = {}",exposer);
        if (exposer.isExposed()) {

            String md5 = exposer.getMd5();
            long phone = 12713219046L;
            try {
                SeckillExecution seckillExecution = seckillService.executeSeckill(stockId, phone, md5);
                logger.info("seckillExecution = {}", seckillExecution);
            } catch (RepeatKillException e1) {
                logger.error(e1.getMessage());
            } catch (SeckillCloseException e2) {
                logger.error(e2.getMessage());
            } catch (SeckillException e) {
                logger.error(e.getMessage());
            }
        } else {
            logger.warn("未开启秒杀或秒杀已结束");
        }

    }



}