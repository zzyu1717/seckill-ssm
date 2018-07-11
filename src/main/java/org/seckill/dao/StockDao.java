package org.seckill.dao;

import org.apache.ibatis.annotations.Param;
import org.seckill.entity.Stock;

import java.util.Date;
import java.util.List;

public interface StockDao {
    /**
     * 减库存
     * @param stockId 秒杀商品库存id
     * @param killTime 秒杀开始时间
     * @return 更新的行数
     */
    int reduceNumber(@Param("stockId") long stockId, @Param("killTime") Date killTime);

    /**
     * 根据id查询秒杀商品
     * @param stockId
     * @return
     */
    Stock queryById(long stockId);

    /**
     * 根据偏移量查询秒杀商品列表
     * @param offset
     * @param limit
     * @return
     */
    List<Stock> queryAll(@Param("offset") int offset, @Param("limit") int limit);

}
