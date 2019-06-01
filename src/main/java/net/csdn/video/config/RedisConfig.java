package net.csdn.video.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;

@Configuration
public class RedisConfig {

    @Value("${spring.redis.host}")
    private String host;
    @Value("${spring.redis.port}")
    private int port;

    @Bean
    RedisConnectionFactory connectionFactory(){
        RedisStandaloneConfiguration configuration = new RedisStandaloneConfiguration(host, port);
        LettuceConnectionFactory connectionFactory = new LettuceConnectionFactory(configuration);
        return connectionFactory;
    }
    @Bean
    RedisTemplate redisTemplate(){
        StringRedisTemplate redisTemplate = new StringRedisTemplate(connectionFactory());
        return redisTemplate;
    }
}
