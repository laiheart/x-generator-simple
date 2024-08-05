package ${basePackage}.db.service;


import ${basePackage}.model.${entityName};
import ${basePackage}.request.${entityPackage}.${entityName}QueryReq;
import ${basePackage}.respone.${entityName}Vo;

import java.util.List;

/**
 * @author ${author}
 * @Date ${createDate}
 */
public interface I${entityName}Service {

    Integer insert(${entityName} ${lowerFirstEntityName});

    List<${entityName}Vo> page(${entityName}QueryReq ${lowerFirstEntityName}QueryReq);

    ${entityName} getById(Integer id);

    Integer edit(${entityName} ${lowerFirstEntityName});

  List<${entityName}> list(List<String> archiveNoPrefixList);
}
