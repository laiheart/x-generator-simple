package com.x.generator.TemplateMaker.constant;

/**
 * @author lsx
 * @date 2024-07-14
 */
public interface FileHandlerTypeConst {
    String[] BASE_HANDLER = new String[]{"Controller", "Business", "Service", "Mapper"};
    String[] NO_PK_MODEL_HANDLER = new String[]{"AddReq", "QueryReq"};
    String[] HAS_PK_MODEL_HANDLER = new String[]{"EditReq", "Vo", "VO"};
}