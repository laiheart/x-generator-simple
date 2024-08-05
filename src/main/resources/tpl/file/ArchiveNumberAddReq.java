package com.ection.platform.drivermanager.request.archivenumber;

import com.ection.platform.drivermanager.model.ArchiveNumber;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;
import java.util.Date;

/**
 * 转入档案编号表添加实体
 *
 * @author lsx
 * @date 2024-07-30
 */
@Schema(description = "转入档案编号表添加实体")
@Data
public class ArchiveNumberAddReq implements Serializable {

  private static final long serialVersionUID = 1L;

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

  public static ArchiveNumber toArchiveNumber(ArchiveNumberAddReq archiveNumberAddReq) {
    ArchiveNumber archiveNumber = new ArchiveNumber();
    BeanUtils.copyProperties(archiveNumberAddReq, archiveNumber);
    archiveNumber.setCreateDate(new Date());
    return archiveNumber;
  }

}
