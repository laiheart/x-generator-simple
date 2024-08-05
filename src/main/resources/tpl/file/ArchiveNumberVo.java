package com.ection.platform.drivermanager.respone;

import com.ection.platform.drivermanager.model.ArchiveNumber;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.beans.BeanUtils;


/**
 * 转入档案编号表显示实体
 *
 * @author lsx
 * @date 2024-07-30
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ArchiveNumberVo extends ArchiveNumber {
  /**
   * 是否已被关联
   */
  @Schema(description = "是否已被关联")
  private Boolean associated;

  public static ArchiveNumberVo toArchiveNumberVo(ArchiveNumber archiveNumber) {
    ArchiveNumberVo archiveNumberVo = new ArchiveNumberVo();
    BeanUtils.copyProperties(archiveNumber, archiveNumberVo);
    return archiveNumberVo;
  }
}
