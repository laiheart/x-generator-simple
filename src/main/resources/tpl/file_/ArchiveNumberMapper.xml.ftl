<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="${basePackage}.db.dao.${entityName}Mapper">

<resultMap id="BaseResultMap" type="${basePackage}.model.${entityName}">
    <#if pkFieldInfo??>
        <id property="${pkFieldInfo.propertyName}" column="${pkFieldInfo.columnName}" jdbcType="${pkFieldInfo.columnTypeName}"/>
    </#if>
    <#list fieldInfos as field>
        <result property="${field.propertyName}" column="${field.columnName}" jdbcType="${field.columnTypeName}"/>
    </#list>
</resultMap>

<select id="page" resultType="${basePackage}.vo.${entityName}Vo">
    select <#if pkFieldInfo??>${pkFieldInfo.columnName} ${pkFieldInfo.propertyName},</#if>
    <#list fieldInfos as field>
        ${field.columnName} ${field.propertyName}<#if field?is_last == false>,</#if>
    </#list>
</select>

</mapper>
