package com.x.generator.core;

import lombok.Data;

/**
 * @author lsx
 * @date 2024-07-13
 */
@Data
public class MetaDataContainer {
    private EntityMetaData entity;
    private EntityMetaData addReq;
    private EntityMetaData queryReq;
    private EntityMetaData editReq;
    private EntityMetaData vo;

}
