package com.bfd.utils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bfd.config.RedisUtil;
import io.codis.jodis.JedisResourcePool;
import io.codis.jodis.RoundRobinJedisPool;
import redis.clients.jedis.Jedis;

import java.util.HashMap;
import java.util.Map;

/**
 * @author: bing.shen
 * @date: 2018/9/7 17:10
 * @Description:
 */
public class Test {

    public void test(){
        StringBuffer sb = new StringBuffer();
        sb.append("local n = tonumber(ARGV[1]) ")
        .append("if not n or n == 0 then return 0 end ")
        .append("local vals = redis.call('HMGET', KEYS[1], 'Total', 'Booked');")
        .append("local total = tonumber(vals[1]) local blocked =tonumber(vals[2]) ")
        .append("if not total or not blocked then return 0 end ")
        .append("if blocked + n <= total then redis.call('HINCRBY', KEYS[1], 'Booked', n) ")
        .append("return n end return 0;");
        System.out.println(RedisUtil.eval(sb.toString(), 1, "aaa", "1"));
        System.out.println("Booked---------" + RedisUtil.hmget("aaa", "Booked"));
    }

    public static void main(String[] args) {
        JedisResourcePool jedisPool = RoundRobinJedisPool.create()
                .curatorClient("172.24.3.105:2181,172.24.3.106:2181,172.24.3.107:2181", 30000).zkProxyDir("/zk/codis/db_xhs-codis/proxy").build();
        try (Jedis jedis = jedisPool.getResource()) {
            /*jedis.set("foo", "bar");
            String value = jedis.get("foo");
            System.out.println(value);*/
            /*Map<String, String> map = new HashMap<>();
            map.put("Total", "10");
            map.put("Booked", "0");
            jedis.hmset("limit_device_10018", map);*/
            //System.out.println(jedis.hmget("83", "Total"));
            //System.out.println(jedis.hmget("83", "Booked"));
            //String value = jedis.get("limit_ip_".concat("181"));
            //System.out.println(value);
            /*StringBuffer sb = new StringBuffer();
            sb.append("local n = tonumber(ARGV[1]) ")
            .append("if not n or n == 0 then return 0 end ")
            .append("local vals = redis.call('HMGET', KEYS[1], 'Total', 'Booked');")
            .append("local total = tonumber(vals[1]) local blocked =tonumber(vals[2]) ")
            .append("if not total or not blocked then return 0 end ")
            .append("if blocked + n <= total then redis.call('HINCRBY', KEYS[1], 'Booked', n) ")
            .append("return n end return 0;");
            System.out.println(jedis.eval(sb.toString(), 1, "aaa", "1"));
            System.out.println("Booked---------" + jedis.hmget("aaa", "Booked"));*/

            /*JSONArray jsonArray = new JSONArray();
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("words", "name");
            jsonObject.put("isRequired", 1);
            jsonObject.put("desc", "姓名");
            jsonArray.add(jsonObject);
            jsonObject = new JSONObject();
            jsonObject.put("words", "age");
            jsonObject.put("isRequired", 0);
            jsonObject.put("desc", "年龄");
            jsonArray.add(jsonObject);
            jedis.set("limit_extra_10018", jsonArray.toJSONString());*/
            //System.out.println(jedis.get("limit_set_140"));
            //jedis.del("10018");
            String s = jedis.get("auth_296");
            System.out.println();
            //jedis.expire("10018", 1);
            //System.out.println(jedis.incr("10018"));
        }
    }
}
