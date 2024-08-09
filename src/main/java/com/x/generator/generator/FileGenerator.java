package com.x.generator.generator;


import cn.hutool.core.io.FileUtil;
import com.x.generator.generator.config.GlobalConfig;
import com.x.generator.generator.constant.BaseConst;
import com.x.generator.generator.entity.FileInfo;
import com.x.generator.generator.core.MetaInfoModelBuilder;
import com.x.generator.generator.entity.CommonMetaInfo;
import com.x.generator.generator.entity.MetaInfoModel;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.List;

/**
 * @author lsx
 * @date 2024-07-13
 */
public class FileGenerator {

    public static void main(String[] args) throws IOException, IllegalAccessException {
        generateFile();
    }

    public static void generateFile() throws IOException, IllegalAccessException {
        //配置信息
        GlobalConfig config = new GlobalConfig();
        //180数据库
        config.setDbUrl("jdbc:mysql://192.168.5.180:3306/ection?useSSL=false&serverTimezone=UTC");
        config.setDbUser("root");
        config.setDbPassword("ectiondb_#mysql");
        config.setTableNames("t_my_resource");
        config.setTableIgnorePrefix("t_");

//        //本地数据库
//        config.setDbUrl("jdbc:mysql://localhost:3306/xindada?useSSL=false&serverTimezone=UTC");
//        config.setDbUser("root");
//        config.setDbPassword("root");
//        //需要生成的表
//        config.setTableNames("app");
//        //忽略表前缀
//        config.setTableIgnorePrefix("");

        config.setAuthor("lsx");
        String basePackage = "com.x.generator.g__";
        config.setBasePackage(basePackage);

        // 获取整个项目的根路径
        String projectPath = System.getProperty("user.dir") + File.separator;
        FileInfo fileInfo = new FileInfo();
        // 模板文件基础路径
        fileInfo.setBaseInputPath(projectPath + "src/main/resources/tpl/file_");
        // 基于项目路径下的文件基础输出路径
        String baseDirectory = ".gen";
        // 输出文件基础路径
        fileInfo.setBaseOutputPath(projectPath + baseDirectory);
        // 各种层之间的包名
        fileInfo.setControllerDir("controller");
        //设置模板文件的实体名
        fileInfo.setEntityName("ArchiveNumber");
        //设置dto,vo包名
        fileInfo.setDtoDir("dto/myresource");
        fileInfo.setVoDir("vo");
        // 生成文件
        doGenerate(fileInfo, config);
    }

    public static void doGenerate(FileInfo fileInfo, GlobalConfig config) throws IOException {
        // new 出 Configuration 对象，参数为 FreeMarker 版本号
        Configuration configuration = new Configuration(Configuration.VERSION_2_3_32);
        // 指定模板文件所在的基础路径
        File templateDir = new File(fileInfo.getBaseInputPath());
        configuration.setDirectoryForTemplateLoading(templateDir);
        // 设置模板文件使用的字符集
        configuration.setDefaultEncoding("utf-8");
        //构建所有模型信息
        List<CommonMetaInfo> infoList = MetaInfoModelBuilder.build(config);
        //基础路径
        String baseOutputPath = fileInfo.getBaseOutputPath();
        String[] filenames = templateDir.list();
        if (filenames == null) {
            return;
        }
        for (CommonMetaInfo metaInfo : infoList) {
            // 元信息数据模型
            MetaInfoModel metaInfoModel = (MetaInfoModel) metaInfo;
            String entityName = metaInfoModel.getEntityName();
            for (String filename : filenames) {
                String outputPath = FileInfo.joinPath(baseOutputPath, smartPath(filename, entityName, fileInfo));
                generateFile(filename, outputPath, metaInfo, configuration);
            }
        }
    }

    /**
     * 智能生成新的文件名
     *
     * @param filename
     * @param newEntityName
     * @param fileInfo
     * @return
     */
    private static String smartPath(String filename, String newEntityName, FileInfo fileInfo) {
        filename = filename.replaceAll("\\.ftl", "").replaceAll(fileInfo.getEntityName(), newEntityName);
        if (StringUtils.contains(filename, BaseConst.CONTROLLER)) {
            return FileInfo.joinPath(fileInfo.getControllerDir(), filename);
        } else if (StringUtils.contains(filename, BaseConst.BUSINESS)) {
            return FileInfo.joinPath(fileInfo.getBusinessDir(), filename);
        } else if (StringUtils.contains(filename, BaseConst.MAPPER)) {
            return FileInfo.joinPath(fileInfo.getMapperDir(), filename);
        } else if (StringUtils.contains(filename, BaseConst.SERVICE)) {
            if (filename.contains("Impl") || (filename.startsWith(newEntityName))) {
                return FileInfo.joinPath(fileInfo.getServiceImplDir(), filename);
            }
            return FileInfo.joinPath(fileInfo.getServiceDir(), filename);
        } else if (StringUtils.containsAny(filename, BaseConst.DTO)) {
            return FileInfo.joinPath(fileInfo.getDtoDir(), filename);
        } else if (StringUtils.containsAny(filename, BaseConst.VO)) {
            return FileInfo.joinPath(fileInfo.getVoDir(), filename);
        }
        return FileInfo.joinPath(fileInfo.getModelDir(), filename);
    }

    /**
     * 基于ftl生成文件
     *
     * @param templateInputRelaPath
     * @param outputPath
     * @param model
     * @param configuration
     */
    public static void generateFile(String templateInputRelaPath, String outputPath, Object model, Configuration configuration) {
        // 文件不存在则创建文件和父目录
        if (!FileUtil.exist(outputPath)) {
            FileUtil.touch(outputPath);
        }
        Writer out = null;
        try {
            out = new FileWriter(outputPath);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        // 创建模板对象，加载指定模板
        Template template = null;
        try {
            template = configuration.getTemplate(templateInputRelaPath);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try {
            template.process(model, out);
        } catch (TemplateException | IOException e) {
            throw new RuntimeException(e);
        }
    }
}
