package com.lyqf.bullet.logic.config;

import java.util.ArrayList;
import java.util.List;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.ClusterServersConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisClusterConfiguration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisNode;
import org.springframework.data.redis.connection.jedis.JedisClientConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.util.ClassUtils;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.netty.channel.nio.NioEventLoopGroup;
import redis.clients.jedis.JedisPoolConfig;

/**
 * @author chenlang
 * @date 2022/4/21 2:38 下午
 */

@Configuration
public class RedisConfig {

    @Value("#{'${spring.redis.cluster.nodes}'.split(',')}")
    private List<String> redisCluster;

    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<String, Object>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);

        // 使用Jackson2JsonRedisSerialize 替换默认序列化
        Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer =
            new Jackson2JsonRedisSerializer<Object>(Object.class);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        objectMapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);

        jackson2JsonRedisSerializer.setObjectMapper(objectMapper);

        // 设置value的序列化规则和 key的序列化规则
        redisTemplate.setValueSerializer(jackson2JsonRedisSerializer);
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashValueSerializer(jackson2JsonRedisSerializer); // 很多地方存了实体类，无法转换成序列化字符串
        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }

    @Bean
    public JedisConnectionFactory redisConnectionFactory(JedisPoolConfig jedisPool) {
         //single
//        RedisStandaloneConfiguration configuration = new RedisStandaloneConfiguration();
//        configuration.setPassword("yasnbangweb4");
//        configuration.setHostName("47.93.61.147");
//        configuration.setPort(6379);

        //cluster
        List<RedisNode> redisNodeList = new ArrayList<>();
        redisCluster.forEach(k -> {
            String[] temp = k.split(":");
            redisNodeList.add(new RedisNode(temp[0], Integer.parseInt(temp[1])));
        });
        
        RedisClusterConfiguration configuration =  new RedisClusterConfiguration();
        configuration.setClusterNodes(redisNodeList);
        configuration.setPassword("chenlanglang");

        JedisClientConfiguration.JedisPoolingClientConfigurationBuilder jpcb =
            JedisClientConfiguration.builder().usePooling();
        jpcb.poolConfig(jedisPool);
        JedisClientConfiguration jedisClientConfiguration = jpcb.build();
        return new JedisConnectionFactory(configuration, jedisClientConfiguration);
    }


    @Bean
    public JedisPoolConfig jedisPoolConfig() {
        // 创建连接池对象
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        // 最大空闲数
        jedisPoolConfig.setMaxIdle(50);
        // 连接池最大连接数数据库数量
        jedisPoolConfig.setMaxTotal(50);
        // 连接最大等待时间
        jedisPoolConfig.setMaxWaitMillis(1000);
        // 逐出连接最小空空闲时间
        jedisPoolConfig.setMinEvictableIdleTimeMillis(2000);
        // 每次逐出的最大数量
        jedisPoolConfig.setNumTestsPerEvictionRun(1);
        // 逐出扫描时间的间隔
        jedisPoolConfig.setTimeBetweenEvictionRunsMillis(40000);
        // 是否在从池中取出连接前进行检验,如果检验失败,则从池中去除连接并尝试取出另一个
        jedisPoolConfig.setTestOnBorrow(false);
        // 空闲时检查有效性
        jedisPoolConfig.setTestWhileIdle(false);
        return jedisPoolConfig;
    }

    // @Bean
    // public RedissonClient getRedissonClient() throws Exception {
    // Config config = new Config();
    // ClusterServersConfig clusterServersConfig = config.useClusterServers();
    // for (int i = 0; i < redisCluster.size(); i++) {
    // clusterServersConfig.addNodeAddress("redis://" + redisCluster.get(i));
    // }
    //
    // clusterServersConfig.setPassword("chenlanglang");
    // Codec codec = (Codec)ClassUtils
    // .forName("org.redisson.codec.JsonJacksonCodec", ClassUtils.getDefaultClassLoader()).newInstance();
    // config.setCodec(codec);
    // config.setEventLoopGroup(new NioEventLoopGroup());
    // return Redisson.create(config);
    // }

}
