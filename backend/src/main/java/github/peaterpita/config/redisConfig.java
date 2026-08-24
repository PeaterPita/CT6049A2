
package github.peaterpita.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class redisConfig {

    // #######################################################
    // # Defining new redis configuration so that image data #
    // # (byte arrays) can be cached for 24 hours #
    // # This is used in the caching of cover images #
    // #######################################################
    @Bean
    public RedisTemplate<String, byte[]> redisTemplate(
            RedisConnectionFactory connectFac) {
        RedisTemplate<String, byte[]> template = new RedisTemplate<>();
        template.setConnectionFactory(connectFac);

        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(RedisSerializer.byteArray());

        return template;
    }
}
