package com.x.generator.TemplateMaker.entity;

import lombok.Data;

import java.util.Map;

/**
 * @author lsx
 * @date 2024-07-21
 */
@Data
public class TemplateInfo {

    /**
     * 基础输入路径
     */
    private String inputPath;
    /**
     * 基础输出路径
     */
    private String outputPath;
    /**
     * 替换指定值为xxx
     */
    private Map<String, String> keyPropertyMap;
    /**
     * 处理类型枚举。TemplateInfoHandlerTypeEnum
     */
    private Integer handlerType;
}
