
    private static final long serialVersionUID = 1L;
<#list fieldInfos as field>

    /**
     * ${field.columnComment}
     */
    private ${field.typeName} ${field.propertyName};
</#list>