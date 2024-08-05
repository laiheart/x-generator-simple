package com.x.generator.TemplateMaker.enums;

import lombok.Getter;

/**
 * @author lsx
 * @date 2024-07-31
 */
@Getter
public enum TemplateInfoHandlerTypeEnum {
    /**
     * 模板类型
     */
    BASE_HANDLER(1, "controller,business,service,mapper"),
    MAPPER_XML_HANDLER(2, "mapper.xml"),
    MODEL_HANDLER(10, "MODEL_NAME"),
    NO_PK_MODEL_HANDLER(11, "NO_PK_CONTAIN"),
    HAS_PK_MODEL_HANDLER(12, "HAS_PK_CONTAIN"),
    ;

    private final int key;
    private final String desc;

    TemplateInfoHandlerTypeEnum(int key, String desc) {
        this.key = key;
        this.desc = desc;
    }
}
