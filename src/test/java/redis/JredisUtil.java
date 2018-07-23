package redis;

import org.seckill.entity.Stock;
import redis.clients.jedis.Jedis;

import java.io.*;
import java.util.Date;

public class JredisUtil {


    public static void main(String[] args) {
        Jedis jedis = new Jedis("192.168.43.100",6379);
        //set(jedis);
        get(jedis);

    }

    public static void get(Jedis jedis) {
        String key = "seckill:2000";
        byte[] bytes = jedis.get(key.getBytes());
        try (ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(bytes))) {

            Object object =  ois.readObject();

            System.out.println(object);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void set(Jedis jedis) {
        Stock stock = new Stock();
        stock.setStockId(2000);
        stock.setNumber(30);
        stock.setStartTime(new Date());
        stock.setEndTime(new Date());

        String key = "seckill:"+stock.getStockId();

        try (ByteArrayOutputStream bos = new ByteArrayOutputStream();
             ObjectOutputStream oos = new ObjectOutputStream(bos)) {

            oos.writeObject(stock);

            jedis.set(key.getBytes(),bos.toByteArray());

            System.out.println(bos.toString());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
