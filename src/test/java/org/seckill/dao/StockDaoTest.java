package org.seckill.dao;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.seckill.entity.Stock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;

/**
 * 配置spring和Junit整合，junit启动时加载springIOC容器
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:spring/spring-dao.xml"})
public class StockDaoTest {

    @Autowired
    private StockDao stockDao;

    @Test
    public void reduceNumber() throws Exception {
        int i = stockDao.reduceNumber(1000, new Date(System.currentTimeMillis()));
        System.out.println(i);
    }

    @Test
    public void queryById() throws Exception {
        Stock stock = stockDao.queryById(1000);
        System.out.println(stock);
    }

    @Test
    public void queryAll() throws Exception {
        List<Stock> stocks = stockDao.queryAll(0, 100);
        for (Stock stock : stocks) {
            System.out.println(stock);
        }
    }

}