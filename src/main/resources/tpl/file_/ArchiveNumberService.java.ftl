package ${basePackage}.db.service.impl;

import ${basePackage}.db.dao.${entityName}Mapper;
import ${basePackage}.db.service.I${entityName}Service;
import ${basePackage}.model.${entityName};
import ${basePackage}.request.${entityPackage}.${entityName}QueryReq;
import ${basePackage}.respone.${entityName}Vo;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author ${author}
 * @Date ${createDate}
 */
@Service
public class ${entityName}Service implements I${entityName}Service {

  @Resource
  private ${entityName}Mapper ${lowerFirstEntityName}Mapper;

  @Override
  public Integer insert(${entityName} ${lowerFirstEntityName}) {
    ${lowerFirstEntityName}Mapper.insertSelective(${lowerFirstEntityName});
    return ${lowerFirstEntityName}.getId();
  }

  @Override
  public List<${entityName}Vo> page(${entityName}QueryReq ${lowerFirstEntityName}QueryReq) {
    return ${lowerFirstEntityName}Mapper.page(${lowerFirstEntityName}QueryReq);
  }

  @Override
  public ${entityName} getById(Integer id) {
    return ${lowerFirstEntityName}Mapper.selectByPrimaryKey(id);
  }

  @Override
  public Integer edit(${entityName} ${lowerFirstEntityName}) {
    return ${lowerFirstEntityName}Mapper.updateByPrimaryKeySelective(${lowerFirstEntityName});
  }

  @Override
  public List<${entityName}> list(List<String> archiveNoPrefixList) {
    Example example = new Example(${entityName}.class);
    Example.Criteria criteria = example.createCriteria();
    if (ObjectUtils.isNotEmpty(archiveNoPrefixList)) {
      for (String prefix : archiveNoPrefixList) {
        criteria.orLike("archiveNoLow", prefix + "%");
        criteria.orLike("archiveNoHigh", prefix + "%");
      }
    }
    return ${lowerFirstEntityName}Mapper.selectByExample(example);
  }
}
