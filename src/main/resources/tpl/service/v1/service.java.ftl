package ${basePackage}.db.service;

import ${basePackage}.dto.${lowerFirstEntityName}.${entityName}AddReq;
import ${basePackage}.dto.${lowerFirstEntityName}.${entityName}EditReq;
import ${basePackage}.dto.${lowerFirstEntityName}.${entityName}QueryReq;
import ${basePackage}.vo.${entityName}Vo;

import java.util.List;

/**
 * @author ${author}
 * @Date ${createDate}
 */
public interface I${entityName}Service {

    Long insert(${entityName}AddReq ${lowerFirstEntityName}AddReq);

    List<${entityName}Vo> page(${entityName}QueryReq ${lowerFirstEntityName}QueryReq);

    ${entityName}Vo getById(Long id);

    Integer edit(${entityName}EditReq ${lowerFirstEntityName}EditReq);
}
