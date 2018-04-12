package com.zhph.config.datasource;

import java.lang.reflect.Method;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Aspect
@Order(1)
@Service
public class DynamicDataSourceAspect {

    @Before("@annotation(DataSourceSelector)")
    public void beforeSwitchDS(JoinPoint point){

        //获得当前访问的class
        Class<?> className = point.getTarget().getClass();

        //获得访问的方法名
        String methodName = point.getSignature().getName();
        //得到方法的参数的类型
        Class<?>[] argClass = ((MethodSignature)point.getSignature()).getParameterTypes();
        String dataSource = DynamicDataSourceContextHolder.DEFAULT_DS;
        try {
            // 得到访问的方法对象
            Method method = className.getMethod(methodName, argClass);

            // 判断是否存在@DS注解
            if (method.isAnnotationPresent(DataSourceSelector.class)) {
            	DataSourceSelector annotation = method.getAnnotation(DataSourceSelector.class);
                // 取出注解中的数据源名
                dataSource = annotation.value().getDatasourceName();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 切换数据源
        DynamicDataSourceContextHolder.setDB(dataSource);
    }


    @After("@annotation(DataSourceSelector)")
    public void afterSwitchDS(JoinPoint point){
        DynamicDataSourceContextHolder.clearDB();
    }
}