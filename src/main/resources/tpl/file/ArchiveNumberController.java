package com.ection.platform.drivermanager.controller;

import com.ection.platform.common.base.BaseController;
import com.ection.platform.common.dto.ResultData;
import com.ection.platform.common.dto.TableDataInfo;
import com.ection.platform.drivermanager.business.ArchiveNumberBusiness;
import com.ection.platform.drivermanager.model.ArchiveNumber;
import com.ection.platform.drivermanager.request.archivenumber.ArchiveNumberAddReq;
import com.ection.platform.drivermanager.request.archivenumber.ArchiveNumberEditReq;
import com.ection.platform.drivermanager.request.archivenumber.ArchiveNumberQueryReq;
import com.ection.platform.drivermanager.respone.ArchiveNumberVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 转入档案编号表模块
 *
 * @author lsx
 * @date 2024-07-01
 */
@Tag(name = "转入档案编号表模块")
@RestController
@RequestMapping("/archiveNumber")
@Validated
public class ArchiveNumberController extends BaseController {

  @Resource
  private ArchiveNumberBusiness archiveNumberBusiness;

  /**
   * 新增
   *
   * @param archiveNumberAddReq
   * @return
   */
  @Operation(summary = "新增")
  @PostMapping("/add")
  public ResultData<Integer> add(@RequestBody @Validated ArchiveNumberAddReq archiveNumberAddReq) {
    return ResultData.OK(archiveNumberBusiness.add(archiveNumberAddReq));
  }

  /**
   * 分页查询
   *
   * @param archiveNumberQueryReq
   * @return
   */
  @Operation(summary = "分页查询")
  @GetMapping("/page")
  @ApiResponse(responseCode = "200", description = "成功",
    content = @Content(mediaType = "application/json",
      schema = @Schema(implementation = ArchiveNumberVo.class)))
  public TableDataInfo page(@ParameterObject ArchiveNumberQueryReq archiveNumberQueryReq) {
    startPage(archiveNumberQueryReq);
    return getDataTable(archiveNumberBusiness.page(archiveNumberQueryReq));
  }

  /**
   * 档案规则名称列表
   *
   * @return
   */
  @Operation(summary = "档案规则名称列表。警员账号那个档案规则名称可用这个接口")
  @GetMapping("/list")
  public ResultData<List<ArchiveNumber>> list() {
    return ResultData.OK(archiveNumberBusiness.list());
  }


  /**
   * 根据id回显
   *
   * @param id
   * @return
   */
  @Operation(summary = "根据id回显")
  @GetMapping("/info")
  public ResultData<ArchiveNumberVo> getById(@RequestParam("id") @NotNull Integer id) {
    return ResultData.OK(archiveNumberBusiness.getById(id));
  }

  /**
   * 编辑更新
   *
   * @param archiveNumberEditReq
   * @return
   */
  @Operation(summary = "编辑更新")
  @PostMapping("/edit")
  public ResultData<Integer> edit(@RequestBody @Validated ArchiveNumberEditReq archiveNumberEditReq) {
    return ResultData.OK(archiveNumberBusiness.edit(archiveNumberEditReq));
  }
}
