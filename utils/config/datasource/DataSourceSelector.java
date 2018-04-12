package com.zhph.config.datasource;

import java.lang.annotation.*;
/**
 * 
 * @author roilat-D
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.METHOD })
public @interface DataSourceSelector {
	DataSourceNameList value() default DataSourceNameList.SALARY_DATASOURCE;
}
