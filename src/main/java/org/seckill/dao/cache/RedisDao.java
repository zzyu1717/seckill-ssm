package org.seckill.dao.cache;

import io.protostuff.LinkedBuffer;
import io.protostuff.ProtostuffIOUtil;
import io.protostuff.runtime.RuntimeSchema;
import org.seckill.entity.Stock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

public class RedisDao {

    private JedisPool jedisPool;

    private RuntimeSchema<Stock> schema = RuntimeSchema.createFrom(Stock.class);

    private final Logger logger = LoggerFactory.getLogger(RedisDao.class);

    public RedisDao(String ip, int port) {
        jedisPool = new JedisPool(ip,port);
    }

    public Stock getSeckill(long seckillId) {
        // redis 操作逻辑
        try (Jedis jedis = jedisPool.getResource()) {
            String key = "seckill:"+seckillId;

            /**
             * 反序列化
             */
            byte[] bytes = jedis.get(key.getBytes());

            if (bytes != null) {
                // 空对象
                Stock seckill = schema.newMessage();

                ProtostuffIOUtil.mergeFrom(bytes,seckill,schema);

                return seckill;
            }
        }

        return null;
    }


    public String putSeckill(Stock seckill) {

        try (Jedis jedis = jedisPool.getResource()) {
            String  key = "seckill:"+seckill.getStockId();
            /**
             * 序列化
             */
            byte[] bytes = ProtostuffIOUtil.toByteArray(seckill, schema, LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE));

            // 超时缓存
            int timeout = 60*60;
            String result = jedis.setex(key.getBytes(), timeout, bytes);

            return result;
        }
    }
}
