package com.ection.platform.terminal.config.mybatis;

import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.List;

@Configuration
@Slf4j
public class MyBatisConfig {

  @Resource
  private SqlSessionFactory sqlSessionFactory;

  @Resource
  private PrintSqlInterceptor printSqlInterceptor;

  @PostConstruct
  public void addInterceptor() {
    log.info("mybatis addInterceptor name = PrintSqlInterceptor...");
    List<Interceptor> interceptors = sqlSessionFactory.getConfiguration().getInterceptors();
    sqlSessionFactory.getConfiguration().addInterceptor(printSqlInterceptor);
  }
}
