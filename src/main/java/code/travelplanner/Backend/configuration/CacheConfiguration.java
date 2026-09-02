package code.travelplanner.Backend.configuration;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.*;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class CacheConfiguration {

    @Bean
    public RedisCacheManager cacheManager(RedisConnectionFactory connectionFactory) {

        RedisCacheConfiguration configuration = RedisCacheConfiguration
                .defaultCacheConfig()
                .entryTtl(Duration.ofMinutes(30)) //Automatic expiration timer
                .disableCachingNullValues() // Don't cache null values
                // Cache keys displayed as plain text strings
                .serializeKeysWith(
                        RedisSerializationContext.SerializationPair
                                .fromSerializer(new StringRedisSerializer()))
                // Value put back into DTO objects
                .serializeValuesWith(
                        RedisSerializationContext.SerializationPair
                                .fromSerializer(RedisSerializer.json()));

        // Different Caches for different data
        Map<String, RedisCacheConfiguration> cacheConfigurations = new HashMap<>();

        // waypoint cache, coordinates never change. Store until memory is full then use LRU to remove
        cacheConfigurations.put("waypointCoordinates", configuration.entryTtl(Duration.ZERO));

        // Trip list cache, data lives for 2 hours
        // Cannot be until max-memory, lots of unused trips will be stored alongside hot data
        cacheConfigurations.put("tripList", configuration.entryTtl(Duration.ofHours(2)));

        // Results from searching a waypoint cache, store until memory is full
        cacheConfigurations.put("waypointSearchResults",  configuration.entryTtl(Duration.ZERO));

        return RedisCacheManager
                .builder(connectionFactory)
                .cacheDefaults(configuration)
                .withInitialCacheConfigurations(cacheConfigurations)
                .build();
    }

    @Bean
    public RedisTemplate<String, Long> redisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, Long> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);

        // Serializes key as plain text String
        template.setKeySerializer(new StringRedisSerializer());
        // Serializes Long as plain text String in Redis
        template.setValueSerializer(new GenericToStringSerializer<>(Long.class));

        return template;
    }
}
