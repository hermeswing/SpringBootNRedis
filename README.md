# Spring Boot + PostgreSQL + Redis Cache Sample

### Branch
- main

### 개발 환경
* Tools : STS 4.22.1.RELEASE
* JDK : zulu11.72.19-jdk11.0.23
* Framework : Spring Boot 2.7.18
* Database : PostgreSQL 16.3
* Cache NoSQL : Redis 7.2.5
* Build : Gradle

### 가이드
[[SpringBoot] SpringBoot + Redis Sample](https://hermeslog.tistory.com/789, 참조)

```java
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
```

Redis Cache 는 조회 내용 만 Cache 합니다.
```java
    /**
     * <pre>
     *     value = "Member" :: "Member"라는 이름의 캐시에 메서드의 결과가 저장됩니다.
     *     key = "#id" :: 캐시의 키를 지정합니다. id 파라미터 값이 캐시 키로 사용됩니다.
     *     cacheManager = "cacheManager" :: "cacheManager"라는 이름의 캐시 매니저를 사용
     *     unless = "#result == null" :: 메서드의 결과가 null인 경우 캐시에 저장되지 않도록 설정
     * </pre>
     *
     * @param id
     * @return
     * @Cacheable
     */
    @Override
    @Cacheable( value = "Member", key = "#id", cacheManager = "cacheManager", unless = "#result == null" )
    public Member getMember( Long id ) {
        // 해당 ID에 대한 Member 객체가 존재하지 않으면 예외가 발생하는 것을 방지 하기 위해
        // .orElse(null)
        return redisRepository.findById( id ).orElse( null );
    }
```

