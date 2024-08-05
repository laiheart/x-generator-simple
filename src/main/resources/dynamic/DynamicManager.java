package com.ection.platform.terminal.manager;

import com.ection.platform.terminal.db.dao.DynamicMapper;
import com.ection.platform.terminal.db.service.impl.DynamicService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * 不可重复等通用校验
 *
 * @author lsx
 * @date 2024-07-31
 */
@Component
@Slf4j
public class DynamicManager {
  private static DynamicService dynamicService;

  private static DynamicMapper dynamicMapper;

  @Autowired
  public void initDynamicService(DynamicService dynamicService) {
    log.info("DynamicManager initDynamicService");
    DynamicManager.dynamicService = dynamicService;
  }

  @Autowired
  public void initDynamicMapper(DynamicMapper dynamicMapper) {
    log.info("DynamicManager initDynamicMapper");
    DynamicManager.dynamicMapper = dynamicMapper;
  }

  @Deprecated
  public static DynamicService getDynamicService() {
    return dynamicService;
  }

  public static List<Map<String, Object>> query(DynamicObj dynamicObj) {
    handlerDynamicObj(dynamicObj);
    return dynamicMapper.query(dynamicObj);
  }

  public static boolean exist(DynamicObj dynamicObj) {
    dynamicObj.setSelect("0");
    List<Map<String, Object>> result = query(dynamicObj);
    return ObjectUtils.isNotEmpty(result);
  }

  /**
   * 适配动态sql
   *
   * @param dynamicObj
   */
  private static void handlerDynamicObj(DynamicObj dynamicObj) {
    String select = dynamicObj.getSelect();
    if (StringUtils.isBlank(select)) {
      dynamicObj.setSelect("*");
    }
    //处理whereList
    List<DynamicObj.Where> whereList = dynamicObj.getWhereList();
    for (DynamicObj.Where where : whereList) {
      List<DynamicObj.WhereItem> whereItemList = where.getWhereItemList();
      if (ObjectUtils.isNotEmpty(whereItemList)) {
        //删除第一个查询条件的关联关系
        DynamicObj.WhereItem whereItem = whereItemList.get(0);
        whereItem.setOrAnd("");
      }
      //格式化 whereItem 的value
      whereItemList.forEach(whereItem -> whereItem.setHandledValue(DynamicObj.handlerValue(whereItem.getValue())));
    }
    if (whereList.size() == 0) {
      dynamicObj.setWhereListNull();
    }
  }
}
