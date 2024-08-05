package ${basePackage}.business;

import ${basePackage}.db.service.impl.${entityName}Service;
import ${basePackage}.model.${entityName};
import ${basePackage}.request.${entityPackage}.${entityName}AddReq;
import ${basePackage}.request.${entityPackage}.${entityName}EditReq;
import ${basePackage}.request.${entityPackage}.${entityName}QueryReq;
import ${basePackage}.respone.${entityName}Vo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author ${author}
 * @date ${createDate}
 */
@Service
@Slf4j
public class ${entityName}Business {

  @Resource
  private ${entityName}Service ${lowerFirstEntityName}Service;

  public Integer add(${entityName}AddReq ${lowerFirstEntityName}AddReq) {
    ${entityName} ${lowerFirstEntityName} = ${entityName}AddReq.to${entityName}(${lowerFirstEntityName}AddReq);
    return ${lowerFirstEntityName}Service.insert(${lowerFirstEntityName});
  }

  public List<${entityName}Vo> page(${entityName}QueryReq ${lowerFirstEntityName}QueryReq) {
    return ${lowerFirstEntityName}Service.page(${lowerFirstEntityName}QueryReq);
  }

  public ${entityName}Vo getById(Integer id) {
    ${entityName} ${lowerFirstEntityName} = ${lowerFirstEntityName}Service.getById(id);
    return ${entityName}Vo.to${entityName}Vo(${lowerFirstEntityName});
  }

  public Integer edit(${entityName}EditReq ${lowerFirstEntityName}EditReq) {
    ${entityName} ${lowerFirstEntityName} = ${entityName}EditReq.to${entityName}(${lowerFirstEntityName}EditReq);
    return ${lowerFirstEntityName}Service.edit(${lowerFirstEntityName});
  }

  public List<${entityName}> list() {
    return ${lowerFirstEntityName}Service.list();
  }
}
