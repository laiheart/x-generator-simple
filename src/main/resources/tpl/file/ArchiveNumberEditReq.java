package com.ection.platform.drivermanager.request.archivenumber;

import com.ection.platform.drivermanager.model.ArchiveNumber;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;
import java.util.Date;

/**
 * 转入档案编号表编辑更新实体
 *
 * @author lsx
 * @date 2024-07-30
 */
@Schema(description = "转入档案编号表编辑更新实体")
@Data
public class ArchiveNumberEditReq implements Serializable {

  private static final long serialVersionUID = 1L;

  /**
   * 主键
   */
  @Schema(description = "主键")
  private Integer id;

  /**
   * 规则名称
   */
  @Schema(description = "规则名称")
  private String name;

  /**
   * 转入业务写入的档案编号最小值
   */
  @Schema(description = "转入业务写入的档案编号最小值")
  private String archiveNoLow;

  /**
   * 转入业务写入的档案编号最大值
   */
  @Schema(description = "转入业务写入的档案编号最大值")
  private String archiveNoHigh;

  /**
   * 剩余档案编号提示个数
   */
  @Schema(description = "剩余档案编号提示个数")
  private Integer leftArchiveNoNumber;

  public static ArchiveNumber toArchiveNumber(ArchiveNumberEditReq archiveNumberEditReq) {
    ArchiveNumber archiveNumber = new ArchiveNumber();
    BeanUtils.copyProperties(archiveNumberEditReq, archiveNumber);
    archiveNumber.setUpdateDate(new Date());
    return archiveNumber;
  }
}
