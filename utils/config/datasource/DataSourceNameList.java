package com.zhph.config.datasource;

public enum DataSourceNameList {
	SALARY_DATASOURCE("salaryDS"),//主数据源（薪资数据源）
	BUSI_DATASOURCE("busiDS");//其它数据源(信贷/其它业务数据源)
	private String datasourceName;

	DataSourceNameList(String datasourceName) {
		this.datasourceName = datasourceName;
	}

	public String getDatasourceName() {
		return datasourceName;
	}
}