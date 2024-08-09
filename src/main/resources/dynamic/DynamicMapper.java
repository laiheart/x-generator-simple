package com.ection.platform.terminal.db.dao;

import com.ection.platform.terminal.manager.DynamicObj;
import com.ection.platform.terminal.manager.WhereParams;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @author lsx
 * @date 2024-07-22
 */
@Mapper
public interface DynamicMapper {

  /**
   * @param tableName
   * @param select
   * @param whereParams
   * @return
   */
  @MapKey("id")
  @Deprecated
  List<Map<String, Object>> queryByWhereParams(@Param("tableName") String tableName, @Param("select") String select, @Param("list") List<List<WhereParams>> whereParams);

  @MapKey("id")
  List<Map<String, Object>> query(DynamicObj dynamicObj);

  int update(DynamicObj dynamicObj);
}
