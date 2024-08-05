package ${basePackage}.db.dao;

import ${basePackage}.model.${entityName};
import ${basePackage}.request.${entityPackage}.${entityName}QueryReq;
import ${basePackage}.respone.${entityName}Vo;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface ${entityName}Mapper extends Mapper<${entityName}> {
  List<${entityName}Vo> page(${entityName}QueryReq ${lowerFirstEntityName}QueryReq);
}
