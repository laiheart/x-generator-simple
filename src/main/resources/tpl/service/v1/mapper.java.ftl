package ${basePackage}.db.dao;

import ${basePackage}.dto.${entityPackage}.${entityName}QueryReq;
import ${basePackage}.model.${entityName};
import ${basePackage}.vo.${entityName}Vo;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface ${entityName}Mapper extends Mapper<${entityName}> {
    List<${entityName}Vo> page(${entityName}QueryReq ${lowerFirstEntityName}QueryReq);
}
