package redis;

import redis.clients.jedis.Jedis;

public class JredisUtil {


    public static void main(String[] args) {
        Jedis jedis = new Jedis("192.168.43.100",6379);

        String result = jedis.get("hello");

        System.out.println(result);
    }
}
