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
    from ${tableName}
</select>