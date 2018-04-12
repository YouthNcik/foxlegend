package com.zhph.service.common;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * @author lxp
 * @date 2018年3月7日 上午10:57:56
 * @parameter
 * @return
 */

@Component
@Order(value = 1)
public class StartupRunner implements CommandLineRunner {

	@Autowired
	private BaseService baseService;

	@Override
	public void run(String... ac) throws Exception {
		// 数据字典数据加载到Redis缓存
		baseService.RedisSysConfigType();
	}
}
