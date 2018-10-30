package com.bfd.config;

import io.codis.jodis.JedisResourcePool;
import io.codis.jodis.RoundRobinJedisPool;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;

import java.util.List;
import java.util.Map;

/**
 * @author: bing.shen
 * @date: 2018/9/7 15:03
 * @Description:
 */
@Component
@Slf4j
public class RedisUtil {


    private static String zkAddr;

    private static String zkProxyDir;

    private static int zkSessionTimeoutMs;

    @Value("${codis.zkAddr}")
    public void setZkAddr(String zkAddr) {
        RedisUtil.zkAddr = zkAddr;
    }

    @Value("${codis.zkProxyDir}")
    public void setZkProxyDir(String zkProxyDir) {
        RedisUtil.zkProxyDir = zkProxyDir;
    }

    @Value("${codis.zkSessionTimeoutMs}")
    public void setZkSessionTimeoutMs(int zkSessionTimeoutMs) {
        RedisUtil.zkSessionTimeoutMs = zkSessionTimeoutMs;
    }

    private static JedisResourcePool jedisPool = null;

    /**
     * 初始化Redis连接池
     */
    private static void initialPool(){
        jedisPool = RoundRobinJedisPool.create()
                .curatorClient(zkAddr, zkSessionTimeoutMs)
                .zkProxyDir(zkProxyDir).build();
    }

    /**
     * 在多线程环境同步初始化
     */
    private static synchronized Jedis getJedis() {
        if (jedisPool == null) {
            initialPool();
        }
        return jedisPool.getResource();
    }

    public static String hmset(String key, Map<String, String> hash){
        String result = null;
        Jedis jedis = null;
        try{
            jedis = getJedis();
            result = jedis.hmset(key, hash);
            jedis.persist(key);
        }catch (Exception e){
            log.error(e.getMessage());
        }finally {
            jedis.close();
        }
        return result;
    }

    public static Object eval(String script, int keyCount, String... params) {
        Object result = null;
        Jedis jedis = null;
        try{
            jedis = getJedis();
            result = jedis.eval(script, keyCount, params);
        }catch (Exception e){
            log.error(e.getMessage());
        }finally {
            jedis.close();
        }
        return result;
    }

    public static String set(String key, String value){
        String result = null;
        Jedis jedis = null;
        try{
            jedis = getJedis();
            result = jedis.set(key, value);
            jedis.persist(key);
        }catch (Exception e){
            log.error(e.getMessage());
        }finally {
            jedis.close();
        }
        return result;
    }

    public static String get(String key){
        String result = null;
        Jedis jedis = null;
        try{
            jedis = getJedis();
            result = jedis.get(key);
        }catch (Exception e){
            log.error(e.getMessage());
        }finally {
            jedis.close();
        }
        return result;
    }

    public static Boolean exists(String key){
        Boolean result = null;
        Jedis jedis = null;
        try{
            jedis = getJedis();
            result = jedis.exists(key);
        }catch (Exception e){
            log.error(e.getMessage());
        }finally {
            jedis.close();
        }
        return result;
    }

    public static Long del(String key){
        Long result = null;
        Jedis jedis = null;
        try{
            jedis = getJedis();
            result = jedis.del(key);
        }catch (Exception e){
            log.error(e.getMessage());
        }finally {
            jedis.close();
        }
        return result;
    }

    public static List<String> hmget(String key, String... fields){
        List<String> result = null;
        Jedis jedis = null;
        try{
            jedis = getJedis();
            result = jedis.hmget(key, fields);
        }catch (Exception e){
            log.error(e.getMessage());
        }finally {
            jedis.close();
        }
        return result;
    }

    public static Long incr(String key){
        Long result = null;
        Jedis jedis = null;
        try{
            jedis = getJedis();
            result = jedis.incr(key);
        }catch (Exception e){
            log.error(e.getMessage());
        }finally {
            jedis.close();
        }
        return result;
    }

    public static Long expire(String key, int seconds){
        Long result = null;
        Jedis jedis = null;
        try{
            jedis = getJedis();
            result = jedis.expire(key, seconds);
        }catch (Exception e){
            log.error(e.getMessage());
        }finally {
            jedis.close();
        }
        return result;
    }

    public static String hget(String key, String field){
        String result = null;
        Jedis jedis = null;
        try{
            jedis = getJedis();
            result = jedis.hget(key, field);
        }catch (Exception e){
            log.error(e.getMessage());
        }finally {
            jedis.close();
        }
        return result;
    }

    public static Long sadd(String key, String... members){
        Long result = null;
        Jedis jedis = null;
        try{
            jedis = getJedis();
            result = jedis.sadd(key, members);
            jedis.persist(key);
        }catch (Exception e){
            log.error(e.getMessage());
        }finally {
            jedis.close();
        }
        return result;
    }

    public static Long srem(String key, String... members){
        Long result = null;
        Jedis jedis = null;
        try{
            jedis = getJedis();
            result = jedis.srem(key, members);
        }catch (Exception e){
            log.error(e.getMessage());
        }finally {
            jedis.close();
        }
        return result;
    }
}
