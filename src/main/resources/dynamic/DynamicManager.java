package com.ection.platform.terminal.manager;

import com.ection.platform.common.exception.BusinessException;
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

  /**
   * 查询数据
   *
   * @param dynamicObj
   * @return
   */
  public static List<Map<String, Object>> query(DynamicObj dynamicObj) {
    handlerDynamicObj(dynamicObj);
    return dynamicMapper.query(dynamicObj);
  }

  /**
   * 查询数据是否存在
   *
   * @param dynamicObj
   * @return
   */
  public static boolean exist(DynamicObj dynamicObj) {
    dynamicObj.setSelect("0");
    List<Map<String, Object>> result = query(dynamicObj);
    return ObjectUtils.isNotEmpty(result);
  }

  /**
   * 更新字段
   *
   * @param dynamicObj
   * @return
   */
  public static int update(DynamicObj dynamicObj) {
    handlerDynamicObj(dynamicObj);
    return dynamicMapper.update(dynamicObj);
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
    //如果whereList大小为0，则设置为null
    if (whereList.size() == 0) {
      dynamicObj.setWhereListNull();
    }
    handlerSet(dynamicObj);
  }

  /**
   * 格式化更新语句
   *
   * @param dynamicObj
   */
  private static void handlerSet(DynamicObj dynamicObj) {
    List<DynamicObj.Set> setList = dynamicObj.getSetList();
    if (ObjectUtils.isEmpty(setList)) {
      return;
    }
    List<DynamicObj.Where> whereList = dynamicObj.getWhereList();
    if (setList.size() > 1 || whereList.size() > 1) {
      throw new BusinessException("暂时不支持批量更新");
    }
    StringBuilder setSb = new StringBuilder();
    for (DynamicObj.Set set : setList) {
      //遍历更新每个字段值
      List<DynamicObj.SetItem> setItemList = set.getSetItemList();
      for (DynamicObj.SetItem setItem : setItemList) {
        setSb.append(setItem.getColumn()).append(" = ");
        if (null != setItem.getBeforeValue()) {
          setSb.append(setItem.getBeforeValue()).append(" ");
        }
        if (null == setItem.getValue()) {
          setSb.append("NULL");
        } else {
          setSb.append(DynamicObj.handlerValue(setItem.getValue()));
        }
        setSb.append(",");
      }
      if (setSb.length() > 0) {
        setSb.deleteCharAt(setSb.length() - 1);
      }
    }
    dynamicObj.setHandledSet(setSb.toString());
  }
}
