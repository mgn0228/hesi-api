package com.test.back.hesi.web.service.impl;

import com.test.back.hesi.web.service.RedisCacheService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Objects;

@SuppressWarnings("rawtypes")
@Service
public class RedisCacheServiceImpl implements RedisCacheService {
	private final RedisTemplate redisTemplate;

	@Autowired
	public RedisCacheServiceImpl(@Qualifier("redisTemplate") RedisTemplate redisTemplate) {
		this.redisTemplate = redisTemplate;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void setValue(String key, Object value, long secondes) {
		redisTemplate.opsForValue().set(key, value, Duration.ofSeconds(secondes).toMillis());
	}

	@Override
	public String getValue(String key) {
		Object data = redisTemplate.opsForValue().get(key);
		return Objects.nonNull(data) ? data.toString() : null;
	}
}
