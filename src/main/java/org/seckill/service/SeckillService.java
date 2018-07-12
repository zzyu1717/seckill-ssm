package org.seckill.service;

import org.seckill.dto.Exposer;
import org.seckill.dto.SeckillExecution;
import org.seckill.entity.Stock;
import org.seckill.exception.RepeatKillException;
import org.seckill.exception.SeckillCloseException;
import org.seckill.exception.SeckillException;

import java.util.List;

/**
 * 业务接口：站在“使用者”角度设计接口
 * 主要三个方面：方法定义粒度，参数，返回类型
 */
public interface SeckillService {

    /**
     * 获取秒杀商品列表
     * @return
     */
    List<Stock> getSeckillList();

    /**
     * 获取单个秒杀商品
     * @param stockId
     * @return
     */
    Stock getById(long stockId);

    /**
     * 秒杀开启输出秒杀接口地址
     * 否则输出系统时间和秒杀时间
     * @param stockId
     */
    Exposer exportSeckillUrl(long stockId);

    /**
     * 执行秒杀操作
     * @param stockId
     * @param userPhone
     * @param md5
     */
    SeckillExecution executeSeckill(long stockId, long userPhone, String md5)
    throws SeckillException, RepeatKillException, SeckillCloseException;
}
