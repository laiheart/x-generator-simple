package com.x.generator.generator.entity;

import cn.hutool.core.date.DateUtil;
import lombok.Data;

import java.util.Date;

/**
 * @author lsx
 * @date 2024-07-13
 */
@Data
public class CommonMetaInfo {

    private String basePackage;
    private String author;
    private String lowerFirstEntityName;
    private String createDate;

    {
        createDate = DateUtil.formatDate(new Date());
    }
}
