
    private static final long serialVersionUID = 1L;
<#if pkFieldInfo??>

    /**
     * ${pkFieldInfo.columnComment}
     */
    private ${pkFieldInfo.typeName} ${pkFieldInfo.propertyName};
</#if>
<#list fieldInfos as field>

    /**
     * ${field.columnComment}
     */
    private ${field.typeName} ${field.propertyName};
</#list>