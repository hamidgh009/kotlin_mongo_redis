package com.example.demo

import org.apache.naming.SelectorContext
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.cache.CacheManager
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.PropertySource
import org.springframework.core.env.Environment
import org.springframework.data.redis.cache.RedisCacheManager
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.serializer.StringRedisSerializer

@Configuration
class AppConfiguration {

    @Autowired
    val env : Environment? = null

    @Bean
    fun jedisConnectionFactory(): JedisConnectionFactory {
        val factory = JedisConnectionFactory()
        factory.usePool = true
        return factory
    }

    @Bean
    fun redisTemplate(): RedisTemplate<Any, Any> {
        val redisTemplate = RedisTemplate<Any, Any>()
        redisTemplate.connectionFactory = jedisConnectionFactory()
        redisTemplate.hashValueSerializer = StringRedisSerializer()
        redisTemplate.keySerializer = StringRedisSerializer()
        redisTemplate.hashKeySerializer = StringRedisSerializer()

        return redisTemplate
    }


    @Bean
    fun cacheManager(): CacheManager {
        val cacheManager = RedisCacheManager(redisTemplate())
        cacheManager.setDefaultExpiration(env!!.getProperty("redis.expiration_time").toLong())
        return cacheManager
    }

}