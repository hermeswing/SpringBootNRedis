package ezpus.sample.config;

import java.time.Duration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;

@Configuration
@EnableCaching  // 어노테이션을 통해 캐싱 기능 사용 등록
public class RedisConfig {

    @Value( "${spring.redis.host}" )
    private String host;

    @Value( "${spring.redis.port}" )
    private int port;

    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        return new LettuceConnectionFactory( host, port );
    }

    @Bean
    public CacheManager cacheManager() {
        RedisCacheManager.RedisCacheManagerBuilder builder = RedisCacheManager.RedisCacheManagerBuilder
                .fromConnectionFactory( redisConnectionFactory() );

        RedisCacheConfiguration configuration = RedisCacheConfiguration.defaultCacheConfig()
                // JSON 형태로 Serialization 적용
                .serializeValuesWith(
                        RedisSerializationContext.SerializationPair.fromSerializer( new GenericJackson2JsonRedisSerializer() ) ) // Value
                // 데이터가 null일 경우 캐싱하지 않음
                .disableCachingNullValues()
                // 유효기간 설정
                .entryTtl( Duration.ofMinutes( 30L ) );

        builder.cacheDefaults( configuration );

        return builder.build();
    }

}
