package ${basePackage}.controller;

import com.ection.platform.common.base.BaseController;
import com.ection.platform.common.dto.ResultData;
import com.ection.platform.common.dto.TableDataInfo;
import ${basePackage}.business.${entityName}Business;
import ${basePackage}.dto.${entityPackage}.*;
import ${basePackage}.vo.${entityName}Vo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;

/**
 * ${tableComment}模块
 *
 * @author lsx
 * @date 2024-07-01
 */
@Api(tags = {"${tableComment}模块"})
@RestController
@RequestMapping("/${lowerFirstEntityName}")
@Validated
public class ${entityName}Controller extends BaseController {

    @Resource
    private ${entityName}Business ${lowerFirstEntityName}Business;

    /**
     * 大头贴相框模板新增接口
     *
     * @param ${lowerFirstEntityName}AddReq
     * @return
     */
    @ApiOperation("大头贴相框模板新增接口")
    @PostMapping("/add")
    public ResultData<Long> add(@RequestBody @Validated ${entityName}AddReq ${lowerFirstEntityName}AddReq) {
        return ResultData.OK(${lowerFirstEntityName}Business.add(${lowerFirstEntityName}AddReq));
    }

    /**
     * 分页查询
     *
     * @param ${lowerFirstEntityName}QueryReq
     * @return
     */
    @ApiOperation(value = "分页查询", notes = "返回值的rows里的每一个对象，可参考根据id回显返回值的data")
    @GetMapping("/page")
    public TableDataInfo page(${entityName}QueryReq ${lowerFirstEntityName}QueryReq) {
        startPage(${lowerFirstEntityName}QueryReq);
        return getDataTable(${lowerFirstEntityName}Business.page(${lowerFirstEntityName}QueryReq));
    }

    /**
     * 根据id回显
     *
     * @param id
     * @return
     */
    @ApiOperation("根据id回显")
    @GetMapping("/getById")
    public ResultData<${entityName}Vo> getById(@RequestParam("id") @NotNull Long id) {
        return ResultData.OK(${lowerFirstEntityName}Business.getById(id));
    }

    /**
     * 编辑更新
     *
     * @param ${lowerFirstEntityName}EditReq
     * @return
     */
    @ApiOperation("编辑更新")
    @PostMapping("/edit")
    public ResultData<Integer> edit(@RequestBody @Validated ${entityName}EditReq ${lowerFirstEntityName}EditReq) {
        return ResultData.OK(${lowerFirstEntityName}Business.edit(${lowerFirstEntityName}EditReq));
    }
}
