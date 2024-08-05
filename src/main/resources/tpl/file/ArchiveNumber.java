package com.ection.platform.drivermanager.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

/**
 * 转入档案编号表
 *
 * @author lsx
 * @date 2024-07-30
 */
@Schema(description = "转入档案编号表")
@Data
@Table(name = "t_drivermanager_archive_number")
public class ArchiveNumber implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @Schema(description = "主键")
    @Id
    @Column(name = "id")
    @GeneratedValue(generator = "JDBC")
    private Integer id;

    /**
     * 规则名称
     */
    @Schema(description = "规则名称")
    @Column(name = "name")
    private String name;

    /**
     * 转入业务写入的档案编号最小值
     */
    @Schema(description = "转入业务写入的档案编号最小值")
    @Column(name = "archive_no_low")
    private String archiveNoLow;

    /**
     * 转入业务写入的档案编号最大值
     */
    @Schema(description = "转入业务写入的档案编号最大值")
    @Column(name = "archive_no_high")
    private String archiveNoHigh;

    /**
     * 剩余档案编号提示个数
     */
    @Schema(description = "剩余档案编号提示个数")
    @Column(name = "left_archive_no_number")
    private Integer leftArchiveNoNumber;

    /**
     * 创建时间
     */
    @Schema(description = "创建时间")
    @Column(name = "create_date")
    private Date createDate;

    /**
     * 更新时间
     */
    @Schema(description = "更新时间")
    @Column(name = "update_date")
    private Date updateDate;

}
