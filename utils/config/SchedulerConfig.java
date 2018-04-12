package com.zhph.config;

import java.io.IOException;
import java.util.Properties;

import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

import com.zhph.scheduled.MyScheduler;

@Configuration
public class SchedulerConfig  implements ApplicationListener<ContextRefreshedEvent> {

	@Value("${quartz.scheduler.instanceName}")
	private String quartzInstanceName;
	
	@Value("${org.quartz.dataSource.myDS.driver}")
	private String myDSDriver;
	
	@Value("${org.quartz.dataSource.myDS.URL}")
	private String myDSURL;
	
	@Value("${org.quartz.dataSource.myDS.user}")
	private String myDSUser;
	
	@Value("${org.quartz.dataSource.myDS.password}")
	private String myDSPassword;
	
	@Value("${org.quartz.dataSource.myDS.maxConnections}")
	private String myDSMaxConnections;

    @Autowired
	public MyScheduler myScheduler;
	
	 /**
     * 定时任务集群配置
     * 设置属性
     *
     * @return
     * @throws IOException
     */
    private Properties quartzProperties() throws IOException {
        Properties prop = new Properties();
        // org.quartz.scheduler.instanceName属性可为任何值，用在 JDBC JobStore
        // 中来唯一标识实例，但是所有集群节点中必须相同。
        prop.put("quartz.scheduler.instanceName", quartzInstanceName);
        // instanceId 属性为 AUTO即可，基于主机名和时间戳来产生实例 ID。
        prop.put("org.quartz.scheduler.instanceId", "AUTO");
        // Quartz内置了一个“更新检查”特性，因此Quartz项目每次启动后都会检查官网，Quartz是否存在新版本。这个检查是异步的，不影响Quartz项目本身的启动和初始化。
        // 设置org.quartz.scheduler.skipUpdateCheck的属性为true来跳过更新检查
        prop.put("org.quartz.scheduler.skipUpdateCheck", "true");
        prop.put("org.quartz.scheduler.jmx.export", "true");

        // org.quartz.jobStore.class属性为 JobStoreTX，将任务持久化到数据中。因为集群中节点依赖于数据库来传播
        // Scheduler 实例的状态，你只能在使用 JDBC JobStore 时应用 Quartz 集群。
        // 这意味着你必须使用 JobStoreTX 或是 JobStoreCMT 作为 Job 存储；你不能在集群中使用 RAMJobStore。
        prop.put("org.quartz.jobStore.class", "org.quartz.impl.jdbcjobstore.JobStoreTX");
        prop.put("org.quartz.jobStore.driverDelegateClass", "org.quartz.impl.jdbcjobstore.StdJDBCDelegate");
        prop.put("org.quartz.jobStore.tablePrefix", "QRTZ_");
        // isClustered属性为 true，你就告诉了Scheduler实例要它参与到一个集群当中。这一属性会贯穿于调度框架的始终
        prop.put("org.quartz.jobStore.isClustered", "true");

        
        // clusterCheckinInterval属性定义了Scheduler实例检入到数据库中的频率(单位：毫秒)。Scheduler查是否其他的实例到了它们应当检入的时候未检入；
        // 这能指出一个失败的 Scheduler 实例，且当前 Scheduler 会以此来接管任何执行失败并可恢复的 Job。
        // 通过检入操作，Scheduler 也会更新自身的状态记录。clusterChedkinInterval 越小，Scheduler
        // 节点检查失败的 Scheduler 实例就越频繁。默认值是 15000 (即15 秒)
        prop.put("org.quartz.jobStore.clusterCheckinInterval", "20000");
        prop.put("org.quartz.jobStore.dataSource", "myDS");
        prop.put("org.quartz.jobStore.maxMisfiresToHandleAtATime", "1");
        prop.put("org.quartz.jobStore.misfireThreshold", "120000");
        prop.put("org.quartz.jobStore.txIsolationLevelSerializable", "false");
        prop.put("org.quartz.jobStore.selectWithLockSQL", "SELECT * FROM {0}LOCKS WHERE LOCK_NAME = ? FOR UPDATE");

        prop.put("org.quartz.threadPool.class", "org.quartz.simpl.SimpleThreadPool");
        prop.put("org.quartz.threadPool.threadCount", "10");
        prop.put("org.quartz.threadPool.threadPriority", "5");
        prop.put("org.quartz.threadPool.threadsInheritContextClassLoaderOfInitializingThread", "true");

        prop.put("org.quartz.dataSource.myDS.driver", myDSDriver);
        prop.put("org.quartz.dataSource.myDS.URL", myDSURL);
        prop.put("org.quartz.dataSource.myDS.user", myDSUser);
        prop.put("org.quartz.dataSource.myDS.password", myDSPassword);
        prop.put("org.quartz.dataSource.myDS.maxConnections", myDSMaxConnections);

        prop.put("org.quartz.plugin.triggHistory.class", "org.quartz.plugins.history.LoggingJobHistoryPlugin");
        prop.put("org.quartz.plugin.shutdownhook.class", "org.quartz.plugins.management.ShutdownHookPlugin");
        prop.put("org.quartz.plugin.shutdownhook.cleanShutdown", "true");
        return prop;
    }
	
    
    @Bean 
    public SchedulerFactoryBean schedulerFactoryBean() throws IOException { 
        SchedulerFactoryBean factory = new SchedulerFactoryBean(); 
        // this allows to update triggers in DB when updating settings in config file: 
        //用于quartz集群,QuartzScheduler 启动时更新己存在的Job，这样就不用每次修改targetObject后删除qrtz_job_details表对应记录了 
        factory.setOverwriteExistingJobs(true); 
        //用于quartz集群,加载quartz数据源 
        //factory.setDataSource(dataSource);   
        //QuartzScheduler 延时启动，应用启动完10秒后 QuartzScheduler 再启动 
        factory.setStartupDelay(10);
        //用于quartz集群,加载quartz数据源配置 
        factory.setQuartzProperties(quartzProperties());
        factory.setAutoStartup(true);
        
        return factory; 
    }


	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		   try {
	            myScheduler.scheduleJobs();
	        } catch (SchedulerException e) {
	            e.printStackTrace();
	        }
		
	}
    

}