package com.x.generator.TemplateMaker;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.ObjUtil;
import com.x.generator.TemplateMaker.entity.TemplateConfig;
import com.x.generator.TemplateMaker.entity.TemplateInfo;
import com.x.generator.TemplateMaker.enums.TemplateInfoHandlerTypeEnum;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * 模板生成工具
 *
 * @author lsx
 * @date 2024-07-21
 */
public class TemplateFileMaker {

    static {
        //初始化数据
        initData();
    }

    /**
     * 不同文件类型预制写入内容
     */
    private static String MODEL_HANDLER_CONTENT;
    private static String NO_PK_MODEL_HANDLER_CONTENT;
    private static String HAS_PK_MODEL_HANDLER_CONTENT;
    private static String MAPPER_XML_HANDLER_CONTENT;
    private static String TABLE_COMMENT_CONTENT;
    private final static String LINE_SYMBOL = "\n";


    public static void main(String[] args) {
        String projectPath = System.getProperty("user.dir");
        Map<String, String> map = new HashMap<>(16);
        //日期
        String createDateRegex = "\\d{4}-(0[1-9]|1[012])-(0[1-9]|[12][0-9]|3[01])";
        map.put(createDateRegex, "createDate");
        //作者
        String author = "lsx";
        map.put(author, "author");
        //基础包名
        String basePackage = "com.ection.platform.drivermanager";
        map.put(basePackage, "basePackage");
        //表名
        String tableName = "t_drivermanager_archive_number";
        map.put(tableName, "tableName");
        //实体名
        String entityName = "ArchiveNumber";
        map.put(entityName, "entityName");
        //实体变量名
        String entityVarName = "archiveNumber";
        map.put(entityVarName, "lowerFirstEntityName");
        //实体包名
        String entityPackage = "archivenumber";
        map.put(entityPackage, "entityPackage");
        //包装属性名为${xxx}
        for (Map.Entry<String, String> entry : map.entrySet()) {
            String value = entry.getValue();
            entry.setValue(getTemplateVal(value));
        }
        TemplateConfig config = new TemplateConfig();
        //输入文件路径
        config.setBaseInputPath(projectPath + "/src/main/resources/tpl/file");
        //输出文件路径
        config.setBaseOutputPath(projectPath + "/src/main/resources/tpl/file_");
        config.setKeyPropertyMap(map);
        doMake(config);
    }

    public static void doMake(TemplateConfig config) {
        String baseInputDir = config.getBaseInputPath();
        String baseOutputDir = config.getBaseOutputPath();
        // 指定文件名对应的模板信息
        Map<String, TemplateInfo> infoMap = config.getTemplateInfoMap();
        File file = new File(baseInputDir);
        for (File item : Objects.requireNonNull(file.listFiles())) {
            //目录递归
            if (item.isDirectory()) {
                String inputPath = new File(baseInputDir, item.getName()).getPath();
                String outputPath = new File(baseOutputDir, item.getName()).getPath();
                config.setBaseInputPath(inputPath);
                config.setBaseOutputPath(outputPath);
                doMake(config);
                continue;
            }
            //获取实际的文件名
            String filename = item.getName();
            TemplateInfo info = infoMap.getOrDefault(filename, new TemplateInfo());
            //构建生成文件信息
            if (ObjUtil.isEmpty(info.getKeyPropertyMap()))
                info.setKeyPropertyMap(config.getKeyPropertyMap());
            if (StringUtils.isBlank(info.getInputPath()))
                info.setInputPath(item.getPath());
            if (StringUtils.isBlank(info.getOutputPath()))
                info.setOutputPath(new File(baseOutputDir, filename + ".ftl").getPath());
            //设置处理的文件类型
            if (null == info.getHandlerType()) {
                info.setHandlerType(TemplateConfig.handlerType(filename));
            }
            doMake(info);
        }
    }

    private static String findModelContent(int handlerType) {
        if (TemplateInfoHandlerTypeEnum.MODEL_HANDLER.getKey() == handlerType) {
            return MODEL_HANDLER_CONTENT;
        }
        if (TemplateInfoHandlerTypeEnum.NO_PK_MODEL_HANDLER.getKey() == handlerType) {
            return NO_PK_MODEL_HANDLER_CONTENT;
        }
        return HAS_PK_MODEL_HANDLER_CONTENT;
    }

    public static void doMake(TemplateInfo... templateInfos) {
        for (TemplateInfo info : templateInfos) {
            //输入文件路径
            String inputPath = info.getInputPath();
            //输出文件路径
            String outputPath = info.getOutputPath();
            Map<String, String> map = info.getKeyPropertyMap();
            //读取文件内容并对内容进行替换
            String srcData = FileUtil.readUtf8String(inputPath);
            //如果是 model 模板
            if (info.getHandlerType() >= TemplateInfoHandlerTypeEnum.MODEL_HANDLER.getKey()) {
                srcData = handlerModel(srcData, info.getHandlerType());
            }
            // 如果是mapperXml 模板
            if (TemplateInfoHandlerTypeEnum.MAPPER_XML_HANDLER.getKey() == info.getHandlerType()) {
                srcData = handlerMapperXml(srcData);
            }
            //值替换
            String destData = replaceAll(srcData, map);
            File ouputFile = new File(outputPath);
            //如果文件所在的目录不存在，则创建
            if (!ouputFile.exists()) {
                FileUtil.touch(ouputFile);
            }
            //生成文件
            FileUtil.writeUtf8String(destData, ouputFile);
        }
    }

    private static String handlerMapperXml(String srcData) {
        StringBuilder sb = new StringBuilder();
        StringBuilder lineSb = new StringBuilder();
        boolean startMapper = false;
        for (char ch : srcData.toCharArray()) {
            lineSb.append(ch);
            if (ch == '\n') {
                String line = lineSb.toString();
                lineSb.setLength(0);
                if (StringUtils.contains(line, "<mapper")) {
                    sb.append(line);
                    sb.append(LINE_SYMBOL);
                    sb.append(MAPPER_XML_HANDLER_CONTENT);
                    sb.append(LINE_SYMBOL);
                    sb.append(LINE_SYMBOL);
                    startMapper = true;
                }
                if (startMapper && !StringUtils.contains(line, "</mapper>")) {
                    continue;
                }
                sb.append(line);
            }
        }
        return sb.toString();
    }

    private static String handlerModel(String srcData, int handlerType) {
        StringBuilder sb = new StringBuilder();
        StringBuilder lineSb = new StringBuilder();
        int contentNum = 0;
        int publicNum = 0;
        int annotationNum = 0;
        for (char ch : srcData.toCharArray()) {
            lineSb.append(ch);
            if (ch == '\n') {
                String line = lineSb.toString();
                //清空数据
                lineSb.setLength(0);
                if (contentNum == 0 && StringUtils.contains(line, "/**")) {
                    contentNum++;
                    continue;
                }
                if (publicNum == 0 && annotationNum == 0 && StringUtils.contains(line, "@")) {
                    sb.append(TABLE_COMMENT_CONTENT);
                    annotationNum++;
                }
                if (StringUtils.contains(line, "public")) {
                    publicNum++;
                    if (annotationNum == 0 && publicNum == 1) {
                        sb.append(TABLE_COMMENT_CONTENT);
                    }
                    if (publicNum == 1) {
                        sb.append(line);
                        sb.append(findModelContent(handlerType));
                        sb.append(LINE_SYMBOL).append(LINE_SYMBOL);
                        continue;
                    }
                }
                if (contentNum > 0 && publicNum == 0 && StringUtils.contains(line, "*")) {
                    continue;
                }
                if (publicNum == 1) {
                    continue;
                }
                sb.append(line);
            }
        }
        if (TemplateInfoHandlerTypeEnum.MODEL_HANDLER.getKey() == handlerType) {
            sb.append("}");
        }
        return sb.toString();
    }

    /**
     * 初始化预定模板
     */
    private static void initData() {
        MODEL_HANDLER_CONTENT = FileUtil.readUtf8String("tpl/.meta/model.ftl");
        NO_PK_MODEL_HANDLER_CONTENT = FileUtil.readUtf8String("tpl/.meta/noPkModel.ftl");
        HAS_PK_MODEL_HANDLER_CONTENT = FileUtil.readUtf8String("tpl/.meta/hasPkModel.ftl");
        TABLE_COMMENT_CONTENT = FileUtil.readUtf8String("tpl/.meta/tableCommentContent.ftl");
        MAPPER_XML_HANDLER_CONTENT = FileUtil.readUtf8String("tpl/.meta/mapperXml.ftl");
    }

    /**
     * 包装为freeMaker变量
     *
     * @param property
     * @return
     */
    private static String getTemplateVal(String property) {
        if (property.contains("\\$\\{")) {
            return property;
        }
        return String.format("\\$\\{%s\\}", property);
    }

    /**
     * 文本值替换
     *
     * @param srcData
     * @param map
     * @return
     */
    private static String replaceAll(String srcData, Map<String, String> map) {
        for (Map.Entry<String, String> entry : map.entrySet()) {
            srcData = srcData.replaceAll(entry.getKey(), entry.getValue());
        }
        return srcData;
    }
}
