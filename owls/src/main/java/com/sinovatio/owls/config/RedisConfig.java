package com.sinovatio.owls.config;

import java.util.logging.Logger;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;

import redis.clients.jedis.JedisPoolConfig;

//@Configuration
//@EnableCaching
public class RedisConfig extends CachingConfigurerSupport {
	 //private static Logger logger = Logger.getLogger(RedisCacheConfig.class);  
     
	    @Bean  
	    @ConfigurationProperties(prefix="spring.redis")  
	    public JedisPoolConfig getRedisConfig(){  
	        JedisPoolConfig config = new JedisPoolConfig();  
	        return config;  
	    }  
	      
	    @Bean  
	    @ConfigurationProperties(prefix="spring.redis")  
	    public JedisConnectionFactory getConnectionFactory(){  
	        JedisConnectionFactory factory = new JedisConnectionFactory();  
	        JedisPoolConfig config = getRedisConfig();  
	        factory.setPoolConfig(config);  
	        //logger.info("JedisConnectionFactory bean init success.");  
	        return factory;  
	    }  
	      
	      
	    @Bean  
	    public RedisTemplate<?, ?> getRedisTemplate(){  
	        RedisTemplate<?,?> template = new StringRedisTemplate(getConnectionFactory());  
	        return template;  
	    }  
     

}
