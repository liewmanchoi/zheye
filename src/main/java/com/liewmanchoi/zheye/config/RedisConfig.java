package com.liewmanchoi.zheye.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import redis.clients.jedis.JedisPoolConfig;

/**
 * @author wangsheng
 * @date 2019/8/10
 */
@Configuration
@EnableTransactionManagement
public class RedisConfig {
  @Bean
  public RedisConnectionFactory redisConnectionFactory() {
    JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
    jedisPoolConfig.setMaxTotal(5);
    jedisPoolConfig.setTestOnBorrow(true);
    jedisPoolConfig.setTestOnReturn(true);

    JedisConnectionFactory connectionFactory = new JedisConnectionFactory();
    connectionFactory.setPoolConfig(jedisPoolConfig);
    connectionFactory.setUsePool(true);
    connectionFactory.setHostName("192.168.29.131");
    connectionFactory.setPort(6379);

    return connectionFactory;
  }

  @Bean(name = "stringIntegerTemplate")
  public RedisTemplate<String, Integer> likeServiceTemplate() {
    RedisConnectionFactory factory = redisConnectionFactory();
    RedisTemplate<String, Integer> redisTemplate = new RedisTemplate<>();
    redisTemplate.setConnectionFactory(factory);
    redisTemplate.setKeySerializer(new StringRedisSerializer());
    redisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer<>(Integer.class));
    return redisTemplate;
  }

  @Bean(name = "likeServiceOperations")
  public SetOperations<String, Integer> likeServiceOperations() {
    RedisTemplate<String, Integer> redisTemplate = likeServiceTemplate();
    return redisTemplate.opsForSet();
  }

  @Bean(name = "followerServiceOperations")
  public ZSetOperations<String, Integer> followerServiceOperations() {
    RedisTemplate<String, Integer> redisTemplate = likeServiceTemplate();
    return redisTemplate.opsForZSet();
  }
}
