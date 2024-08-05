
    private static final long serialVersionUID = 1L;
<#if pkFieldInfo??>

    /**
     * ${pkFieldInfo.columnComment}
     */
    @Id
    @Column(name = "${pkFieldInfo.columnName}")
    @GeneratedValue(generator = "JDBC")
    private ${pkFieldInfo.typeName} ${pkFieldInfo.propertyName};
</#if>
<#list fieldInfos as field>

    /**
     * ${field.columnComment}
     */
    @Column(name = "${field.columnName}")
    private ${field.typeName} ${field.propertyName};
</#list>