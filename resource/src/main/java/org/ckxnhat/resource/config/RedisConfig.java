package org.ckxnhat.resource.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.*;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * @author MinhNhat
 * @email nguyennhat.110120@gmail.com
 * @datetime 2024-07-27 17:49:14.070
 */

@Configuration
public class RedisConfig {
    @Bean
    LettuceConnectionFactory connectionFactory(){
        return new LettuceConnectionFactory();
    }
    @Bean
    public RedisTemplate<String,?> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String,String> template = new RedisTemplate<>();
        // the default is something like a byte serializer, change to string serializer
        template.setDefaultSerializer(new StringRedisSerializer());
        template.setConnectionFactory(redisConnectionFactory);
        template.afterPropertiesSet();
        return template;
    }

    @Bean
    public ValueOperations<String, String> valueOperations(RedisTemplate<String,String> redisTemplate) {
        return redisTemplate.opsForValue();
    }

    @Bean
    public HashOperations<String, String, String> hashOperations(RedisTemplate<String,String> redisTemplate) {
        return redisTemplate.opsForHash();
    }

    @Bean
    public ListOperations<String, String> listOperations(RedisTemplate<String,String> redisTemplate) {
        return redisTemplate.opsForList();
    }

    @Bean
    public SetOperations<String, String> setOperations(RedisTemplate<String,String> redisTemplate) {
        return redisTemplate.opsForSet();
    }

    @Bean
    public ZSetOperations<String, String> zSetOperations(RedisTemplate<String,String> redisTemplate) {
        return redisTemplate.opsForZSet();
    }
}
