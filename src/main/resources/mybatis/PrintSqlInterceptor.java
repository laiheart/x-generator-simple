package com.ection.platform.terminal.config.mybatis;

import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.plugin.*;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * @author lsx
 * @date 2024-08-08
 */
@Slf4j
@Component
@Intercepts({
  @Signature(type = StatementHandler.class, method = "prepare", args = {Connection.class, Integer.class})
})
public class PrintSqlInterceptor implements Interceptor {

  private String First_PageHelper_marking = "First_PageHelper";
  private String Second_PageHelper_marking = "Second_PageHelper";

  private String PAGE_NUM = "pageNum";
  private String PAGE_SIZE = "pageSize";

  private String MARKING = "======================================================================";

  @Override
  public Object intercept(Invocation invocation) throws Throwable {
    StatementHandler statementHandler = (StatementHandler) invocation.getTarget();
    BoundSql boundSql = statementHandler.getBoundSql();
    String sql = boundSql.getSql();
    List<ParameterMapping> parameterMappings = boundSql.getParameterMappings();
    Object parameterObject = boundSql.getParameterObject();
    Class<?> paramsClz = parameterObject.getClass();
    //占位符对应的实际值
    List<Object> values = new ArrayList<>(parameterMappings.size());
    try {
      if (parameterObject instanceof Byte || parameterObject instanceof Short ||
        parameterObject instanceof Integer || parameterObject instanceof Long ||
        parameterObject instanceof Float || parameterObject instanceof Double ||
        parameterObject instanceof Boolean || parameterObject instanceof Character ||
        paramsClz.isPrimitive()) {
        values.add(parameterObject);
      } else if (parameterObject instanceof Map) {
        Map<String, Object> paramsMap = (Map<String, Object>) parameterObject;
        for (ParameterMapping parameterMapping : parameterMappings) {
          String property = parameterMapping.getProperty();
          if (First_PageHelper_marking.equalsIgnoreCase(property)) {
            values.add(paramsMap.get(PAGE_NUM));
          } else if (Second_PageHelper_marking.equalsIgnoreCase(property)) {
            values.add(paramsMap.get(PAGE_SIZE));
          } else {
            values.add(paramsMap.get(property));
          }
        }
      } else {
        for (ParameterMapping parameterMapping : parameterMappings) {
          String property = parameterMapping.getProperty();
          Field field;
          if (First_PageHelper_marking.equalsIgnoreCase(property)) {
            field = paramsClz.getDeclaredField(PAGE_NUM);
          } else if (Second_PageHelper_marking.equalsIgnoreCase(property)) {
            field = paramsClz.getDeclaredField(PAGE_SIZE);
          } else {
            field = paramsClz.getDeclaredField(property);
          }
          field.setAccessible(true);
          values.add(field.get(parameterObject));
          field.setAccessible(false);
        }
      }
    } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
      log.error("PrintSqlInterceptor获取参数值失败", e);
    }
    for (Object value : values) {
      sql = sql.replaceFirst("\\?", handlerValue(value));
    }

    log.info("\n{}最终sql\n{}\n{}", MARKING, sql, MARKING);
    // 继续执行后续操作
    return invocation.proceed();
  }

  @Override
  public Object plugin(Object target) {
    return Plugin.wrap(target, this);
  }

  @Override
  public void setProperties(Properties properties) {

  }

  private String handlerValue(Object value) {
    if (value instanceof String) {
      return "'" + value + "'";
    }
    return value.toString();
  }
}
