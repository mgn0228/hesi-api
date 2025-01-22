package com.test.back.hesi.web.service;

public interface RedisCacheService {
	void setValue(String key, Object value, long secondes);

	String getValue(String key);
}
