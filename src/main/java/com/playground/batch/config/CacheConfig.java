package com.playground.batch.config;

import java.time.Duration;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.jsontype.BasicPolymorphicTypeValidator;
import com.fasterxml.jackson.databind.jsontype.PolymorphicTypeValidator;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.playground.batch.constants.CacheType;

@EnableCaching
@Configuration
public class CacheConfig {
  @Primary
  @Bean(CacheType.ONE_MINUTES)
  RedisCacheManager oneMinutesCacheManager(RedisConnectionFactory redisConnectionFactory) {
    RedisCacheConfiguration redisCacheConfiguration = RedisCacheConfiguration.defaultCacheConfig().entryTtl(Duration.ofMinutes(1))
        .disableCachingNullValues().serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
        .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer(objectMapper())));

    return RedisCacheManager.RedisCacheManagerBuilder.fromConnectionFactory(redisConnectionFactory).cacheDefaults(redisCacheConfiguration).build();
  }

  @Bean(CacheType.TEN_MINUTES)
  RedisCacheManager tenMinutesCacheManager(RedisConnectionFactory redisConnectionFactory) {
    RedisCacheConfiguration redisCacheConfiguration = RedisCacheConfiguration.defaultCacheConfig().entryTtl(Duration.ofMinutes(10))
        .disableCachingNullValues().serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
        .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer(objectMapper())));

    return RedisCacheManager.RedisCacheManagerBuilder.fromConnectionFactory(redisConnectionFactory).cacheDefaults(redisCacheConfiguration).build();
  }

  @Bean(CacheType.ONE_HOUR)
  RedisCacheManager oneHourCacheManager(RedisConnectionFactory redisConnectionFactory) {
    RedisCacheConfiguration redisCacheConfiguration = RedisCacheConfiguration.defaultCacheConfig().entryTtl(Duration.ofHours(1))
        .disableCachingNullValues().serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
        .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer(objectMapper())));

    return RedisCacheManager.RedisCacheManagerBuilder.fromConnectionFactory(redisConnectionFactory).cacheDefaults(redisCacheConfiguration).build();
  }

  @Bean(CacheType.ONE_DAY)
  RedisCacheManager oneDayCacheManager(RedisConnectionFactory redisConnectionFactory) {
    RedisCacheConfiguration redisCacheConfiguration = RedisCacheConfiguration.defaultCacheConfig().entryTtl(Duration.ofDays(1))
        .disableCachingNullValues().serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
        .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer(objectMapper())));

    return RedisCacheManager.RedisCacheManagerBuilder.fromConnectionFactory(redisConnectionFactory).cacheDefaults(redisCacheConfiguration).build();
  }

  @Bean(CacheType.ONE_WEEK)
  RedisCacheManager oneWeekCacheManager(RedisConnectionFactory redisConnectionFactory) {
    RedisCacheConfiguration redisCacheConfiguration = RedisCacheConfiguration.defaultCacheConfig().entryTtl(Duration.ofDays(7))
        .disableCachingNullValues().serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
        .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer(objectMapper())));

    return RedisCacheManager.RedisCacheManagerBuilder.fromConnectionFactory(redisConnectionFactory).cacheDefaults(redisCacheConfiguration).build();
  }

  @Bean(CacheType.ONE_MONTH)
  RedisCacheManager oneMonthCacheManager(RedisConnectionFactory redisConnectionFactory) {
    RedisCacheConfiguration redisCacheConfiguration = RedisCacheConfiguration.defaultCacheConfig().entryTtl(Duration.ofDays(30))
        .disableCachingNullValues().serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
        .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer(objectMapper())));

    return RedisCacheManager.RedisCacheManagerBuilder.fromConnectionFactory(redisConnectionFactory).cacheDefaults(redisCacheConfiguration).build();
  }

  @Bean(CacheType.ONE_YEAR)
  RedisCacheManager oneYearCacheManager(RedisConnectionFactory redisConnectionFactory) {
    RedisCacheConfiguration redisCacheConfiguration = RedisCacheConfiguration.defaultCacheConfig().entryTtl(Duration.ofDays(365))
        .disableCachingNullValues().serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
        .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer(objectMapper())));

    return RedisCacheManager.RedisCacheManagerBuilder.fromConnectionFactory(redisConnectionFactory).cacheDefaults(redisCacheConfiguration).build();
  }

  private ObjectMapper objectMapper() {
    PolymorphicTypeValidator ptv = BasicPolymorphicTypeValidator.builder().allowIfSubType(Object.class).build();
    ObjectMapper objectMapper = new ObjectMapper();

    objectMapper.registerModule(new JavaTimeModule());
    objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    objectMapper.activateDefaultTyping(ptv, ObjectMapper.DefaultTyping.NON_FINAL);

    return objectMapper;
  }
}
