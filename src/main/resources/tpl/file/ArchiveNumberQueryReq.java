package com.ection.platform.drivermanager.request.archivenumber;

import com.ection.platform.common.web.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 转入档案编号表查询实体
 *
 * @author lsx
 * @date 2024-07-30
 */
@Schema(description = "转入档案编号表查询实体")
@EqualsAndHashCode(callSuper = true)
@Data
public class ArchiveNumberQueryReq extends BaseEntity {

  /**
   * 规则名称
   */
  @Schema(description = "规则名称")
  private String name;

  /**
   * 是否已被关联
   */
  @Schema(description = "是否已被关联")
  private Boolean associated;

}
