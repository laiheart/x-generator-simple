package ${basePackage}.controller;

import com.ection.platform.common.base.BaseController;
import com.ection.platform.common.dto.ResultData;
import com.ection.platform.common.dto.TableDataInfo;
import ${basePackage}.business.${entityName}Business;
import ${basePackage}.model.${entityName};
import ${basePackage}.request.${entityPackage}.${entityName}AddReq;
import ${basePackage}.request.${entityPackage}.${entityName}EditReq;
import ${basePackage}.request.${entityPackage}.${entityName}QueryReq;
import ${basePackage}.respone.${entityName}Vo;
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
 * @author ${author}
 * @date ${createDate}
 */
@Tag(name = "转入档案编号表模块")
@RestController
@RequestMapping("/${lowerFirstEntityName}")
@Validated
public class ${entityName}Controller extends BaseController {

  @Resource
  private ${entityName}Business ${lowerFirstEntityName}Business;

  /**
   * 新增
   *
   * @param ${lowerFirstEntityName}AddReq
   * @return
   */
  @Operation(summary = "新增")
  @PostMapping("/add")
  public ResultData<Integer> add(@RequestBody @Validated ${entityName}AddReq ${lowerFirstEntityName}AddReq) {
    return ResultData.OK(${lowerFirstEntityName}Business.add(${lowerFirstEntityName}AddReq));
  }

  /**
   * 分页查询
   *
   * @param ${lowerFirstEntityName}QueryReq
   * @return
   */
  @Operation(summary = "分页查询")
  @GetMapping("/page")
  @ApiResponse(responseCode = "200", description = "成功",
    content = @Content(mediaType = "application/json",
      schema = @Schema(implementation = ${entityName}Vo.class)))
  public TableDataInfo page(@ParameterObject ${entityName}QueryReq ${lowerFirstEntityName}QueryReq) {
    startPage(${lowerFirstEntityName}QueryReq);
    return getDataTable(${lowerFirstEntityName}Business.page(${lowerFirstEntityName}QueryReq));
  }

  /**
   * 档案规则名称列表
   *
   * @return
   */
  @Operation(summary = "档案规则名称列表。警员账号那个档案规则名称可用这个接口")
  @GetMapping("/list")
  public ResultData<List<${entityName}>> list() {
    return ResultData.OK(${lowerFirstEntityName}Business.list());
  }


  /**
   * 根据id回显
   *
   * @param id
   * @return
   */
  @Operation(summary = "根据id回显")
  @GetMapping("/info")
  public ResultData<${entityName}Vo> getById(@RequestParam("id") @NotNull Integer id) {
    return ResultData.OK(${lowerFirstEntityName}Business.getById(id));
  }

  /**
   * 编辑更新
   *
   * @param ${lowerFirstEntityName}EditReq
   * @return
   */
  @Operation(summary = "编辑更新")
  @PostMapping("/edit")
  public ResultData<Integer> edit(@RequestBody @Validated ${entityName}EditReq ${lowerFirstEntityName}EditReq) {
    return ResultData.OK(${lowerFirstEntityName}Business.edit(${lowerFirstEntityName}EditReq));
  }
}
