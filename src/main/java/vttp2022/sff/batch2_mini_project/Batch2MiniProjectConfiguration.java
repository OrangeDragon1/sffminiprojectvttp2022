package vttp2022.sff.batch2_mini_project;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisClientConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class Batch2MiniProjectConfiguration {

    @Value("${spring.redis.host}")
	private String redisHost;
	@Value("${spring.redis.port}")
	private Integer redisPort;
	@Value("${spring.redis.database}")
	private Integer redisDB;
	@Value("${spring.redis.username}")
	private String redisUser;
	@Value("${REDIS_PASSWORD}")
	private String redisPassword;

	@Bean("redislab")
	public RedisTemplate<String, String> createRedisTemplate() {

		final RedisTemplate<String, String> template = new RedisTemplate<>();
		template.setConnectionFactory(createJedisConnectionFactory());
		template.setKeySerializer(new StringRedisSerializer());
		template.setValueSerializer(new StringRedisSerializer());

		return template;
	}

	@Bean
	public JedisConnectionFactory createJedisConnectionFactory() {

		final RedisStandaloneConfiguration redisConfig = new RedisStandaloneConfiguration();
		redisConfig.setHostName(redisHost);
		redisConfig.setPort(redisPort);
		redisConfig.setDatabase(redisDB);
		redisConfig.setUsername(redisUser);
		redisConfig.setPassword(redisPassword);

		final JedisClientConfiguration jedisClientConfig = JedisClientConfiguration
				.builder().build();
		final JedisConnectionFactory jedisFac = new JedisConnectionFactory(redisConfig, jedisClientConfig);
		jedisFac.afterPropertiesSet();
		return jedisFac;
	}
}
