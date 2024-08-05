package com.x.generator.TemplateMaker.entity;

import com.x.generator.TemplateMaker.constant.FileHandlerTypeConst;
import com.x.generator.TemplateMaker.enums.TemplateInfoHandlerTypeEnum;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * @author lsx
 * @date 2024-07-31
 */
@Data
public class TemplateConfig {

    /**
     * 基础输入路径
     */
    private String baseInputPath;
    /**
     * 基础输出路径
     */
    private String baseOutputPath;
    /**
     * 替换指定值为xxx
     */
    private Map<String, String> keyPropertyMap;

    /**
     * 文件名为key的
     */
    private Map<String, TemplateInfo> templateInfoMap = new HashMap<>(2);

    //===================== 规则

    /**
     * mapper.xml后缀
     */
    public static String MAPPER_XML_SUFFIX = "mapper.xml";


    /**
     * 获取文件模板处理方式
     *
     * @param filename
     * @return
     */
    public static Integer handlerType(String filename) {
        filename = filename.toLowerCase();
        //根据文件名判断文件处理类型
        if (filename.endsWith(TemplateConfig.MAPPER_XML_SUFFIX.toLowerCase()))
            return TemplateInfoHandlerTypeEnum.MAPPER_XML_HANDLER.getKey();
        if (isBaseHandler(filename))
            return TemplateInfoHandlerTypeEnum.BASE_HANDLER.getKey();
        if (isNoPKModelHandler(filename)) {
            return TemplateInfoHandlerTypeEnum.NO_PK_MODEL_HANDLER.getKey();
        }
        if (isHasPKModelHandler(filename)) {
            return TemplateInfoHandlerTypeEnum.HAS_PK_MODEL_HANDLER.getKey();
        }
        return TemplateInfoHandlerTypeEnum.MODEL_HANDLER.getKey();
    }

    /**
     * 判断是否为基础模板文件
     *
     * @param filename
     * @return
     */
    public static boolean isBaseHandler(String filename) {
        filename = filename.toLowerCase();
        for (String item : FileHandlerTypeConst.BASE_HANDLER) {
            if (filename.contains(item.toLowerCase())) {
                return true;
            }
        }
        return false;
    }

    public static boolean isNoPKModelHandler(String filename) {
        filename = filename.toLowerCase();
        for (String item : FileHandlerTypeConst.NO_PK_MODEL_HANDLER) {
            if (filename.contains(item.toLowerCase())) {
                return true;
            }
        }
        return false;
    }

    public static boolean isHasPKModelHandler(String filename) {
        filename = filename.toLowerCase();
        for (String item : FileHandlerTypeConst.HAS_PK_MODEL_HANDLER) {
            if (filename.contains(item.toLowerCase())) {
                return true;
            }
        }
        return false;
    }

}
