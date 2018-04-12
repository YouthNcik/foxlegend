package com.zhph.util;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class SpringContextUtils implements ApplicationContextAware {

	private static ApplicationContext applicationContext;

	public void setApplicationContext(ApplicationContext context) throws BeansException {
		applicationContext = context;
	}

	/**
	 * 获取applicationContext对象
	 * 
	 * @return
	 */
	public static ApplicationContext getApplicationContext() {
		int i = 0;
		while (applicationContext == null) {
			try {
				Thread.sleep(5000);
				i++;
				if (i > 1000) {
					break;
				}
			} catch (InterruptedException e) {
			}
		}
		return applicationContext;
	}

	/**
	 * 根据bean的id来查找对象
	 * 
	 * @param id
	 * @return
	 */
	public static Object getBeanById(String id) {
		getApplicationContext();
		return applicationContext.getBean(id);
	}

	/**
	 * 根据bean的class来查找对象
	 * 
	 * @param c
	 * @return
	 */
	public static <T> T getBeanByClass(Class<T> clz) {
		getApplicationContext();
		return applicationContext.getBean(clz);
	}
}
