package com.zhph.config.datasource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

class DynamicDataSourceContextHolder {
    public static final Log log = LogFactory.getLog(DynamicDataSourceContextHolder.class);

    /**
     * 默认数据源
     */
    public static final String DEFAULT_DS = DataSourceNameList.SALARY_DATASOURCE.getDatasourceName();
    private static final ThreadLocal<String> contextHolder = new ThreadLocal<>();

    // 设置数据源名
    public static void setDB(String dbType) {
		log.debug("切换到{" + dbType + "}数据源");
        contextHolder.set(dbType);
    }

    // 获取数据源名
    public static String getDB() {
        return (contextHolder.get());
    }

    // 清除数据源名
    public static void clearDB() {
        contextHolder.remove();
    }
}