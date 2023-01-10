package com.api.freeapi.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/***
 *
 * 布隆过滤器配置
 * @Author chen
 * @Date  9:14
 * @Param
 * @Return
 * @Since version-11

 */

@Component
@ConfigurationProperties(prefix = "bf")
public class BloomFilterProperties {
	/**
	 * 预期插入量
	 */
	private Long expectedInsertions = 1000L;
	/**
	 * 误判率（大于0，小于1.0）
	 */
	private Double fpp = 0.001D;
 
	public Long getExpectedInsertions() {
		return expectedInsertions;
	}
 
	public void setExpectedInsertions(Long expectedInsertions) {
		this.expectedInsertions = expectedInsertions;
	}
 
	public Double getFpp() {
		return fpp;
	}
 
	public void setFpp(Double fpp) {
		this.fpp = fpp;
	}
}