package com.x.generator.TemplateMaker;

import lombok.Data;

import java.util.Map;

/**
 * @author lsx
 * @date 2024-07-21
 */
@Data
public class TemplateInfo {
    private String inputPath;
    private String outputPath;

    private Map<String, String> keyPropertyMap;
}
