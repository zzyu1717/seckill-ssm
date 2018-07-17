package redis;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.Pipeline;

public class PipelineTest {



    public static void main(String[] args) {
        Jedis jedis = new Jedis("192.168.43.100",6379);
        long start = System.currentTimeMillis();
        //testM(jedis);
        testPipeline(jedis);
        long end = System.currentTimeMillis();

        System.out.println(end-start);
    }

    public static void testM(Jedis jedis) {
        for (int i = 0; i < 10000; i++) {
            jedis.hset("key"+i,"field"+i,"value"+i);
        }
    }

    public static void testPipeline(Jedis jedis) {

        for (int i = 0; i < 100; i++) {

            Pipeline pipeline = jedis.pipelined();

            for (int j = i*100; j < (i+1)*100; j++) {
                pipeline.hset("hashkey"+j,"field"+j,"value"+j);
            }
            pipeline.sync();
        }


    }
}
