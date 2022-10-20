package com.djz.preview;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceAutoConfigure;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * xxljob启动类
 * @author hgsoft
 */
@MapperScan(basePackages={"com.djz.**.mapper"})
@EnableScheduling
@SpringBootApplication(exclude = {
        DruidDataSourceAutoConfigure.class
})
@EnableConfigurationProperties
public class XxlJobExecutorApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(XxlJobExecutorApplication.class, args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        // 注意这里要指向原先用main方法执行的Application启动类
        return builder.sources(XxlJobExecutorApplication.class);
    }
}
