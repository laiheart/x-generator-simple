package tpl.file;

import com.ection.platform.common.base.BaseController;
import com.ection.platform.common.dto.ResultData;
import com.ection.platform.common.dto.TableDataInfo;
import com.ection.platform.file.protocol.UploadFileResp;
import com.ection.platform.photo.sticker.business.PhotoStickerFrameTplBusiness;
import com.ection.platform.photo.sticker.dto.photostickerframetpl.*;
import com.ection.platform.photo.sticker.vo.PhotoStickerFrameTplGenVo;
import com.ection.platform.photo.sticker.vo.PhotoStickerFrameTplVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;

/**
 * 相框模板控制器
 *
 * @author lsx
 * @date 2024-07-01
 */
@Api(tags = {"趣味拍照：相框模板"})
@RestController
@RequestMapping("/frameTpl")
@Validated
public class PhotoStickerFrameTplController extends BaseController {

    @Resource
    private PhotoStickerFrameTplBusiness photoStickerFrameTplBusiness;

    /**
     * 大头贴相框模板新增接口
     *
     * @param photoStickerFrameTplAddReq
     * @return
     */
    @ApiOperation("大头贴相框模板新增接口")
    @PostMapping("/add")
    public ResultData<Long> add(@RequestBody @Validated PhotoStickerFrameTplAddReq photoStickerFrameTplAddReq) {
        return ResultData.OK(photoStickerFrameTplBusiness.add(photoStickerFrameTplAddReq));
    }

    /**
     * 上传模板图片
     *
     * @param file
     * @return
     */
    @ApiOperation(value = "上传模板图片", notes = "取data里的filepath")
    @PostMapping("/uploadTplImg")
    public ResultData<UploadFileResp> uploadTplImg(@RequestParam("file") MultipartFile file) {
        return ResultData.OK(photoStickerFrameTplBusiness.upload(file));
    }

    /**
     * 删除文件
     *
     * @return
     */
    @ApiOperation(value = "删除文件")
    @PostMapping("/deleteImg")
    public ResultData<Void> deleteImg(@RequestParam("filepath") String filepath) {
        photoStickerFrameTplBusiness.deleteImg(filepath);
        return ResultData.OK();
    }

    /**
     * 分页查询
     *
     * @param photoStickerFrameTplQueryReq
     * @return
     */
    @ApiOperation(value = "分页查询", notes = "返回值的rows里的每一个对象，可参考根据id回显返回值的data")
    @GetMapping("/page")
    public TableDataInfo page(PhotoStickerFrameTplQueryReq photoStickerFrameTplQueryReq) {
        startPage(photoStickerFrameTplQueryReq);
        return getDataTable(photoStickerFrameTplBusiness.page(photoStickerFrameTplQueryReq));
    }

    /**
     * 根据id回显
     *
     * @param id
     * @return
     */
    @ApiOperation("根据id回显")
    @GetMapping("/getById")
    public ResultData<PhotoStickerFrameTplVo> getById(@RequestParam("id") @NotNull Long id) {
        return ResultData.OK(photoStickerFrameTplBusiness.getById(id));
    }

    /**
     * 编辑更新
     *
     * @param photoStickerFrameTplEditReq
     * @return
     */
    @ApiOperation("编辑更新")
    @PostMapping("/edit")
    public ResultData<Integer> edit(@RequestBody @Validated PhotoStickerFrameTplEditReq photoStickerFrameTplEditReq) {
        return ResultData.OK(photoStickerFrameTplBusiness.edit(photoStickerFrameTplEditReq));
    }

    /**
     * 预览
     *
     * @param photoStickerFrameTplPreviewReq
     * @return
     */
    @ApiOperation(value = "预览", notes = "返回值中的data是base64编码的图片，可使用img标签的src='data:image/png;base64,base64字符串'")
    @PostMapping("/preview")
    public ResultData<String> preview(@RequestBody @Validated PhotoStickerFrameTplPreviewReq photoStickerFrameTplPreviewReq) {
        return ResultData.OK(photoStickerFrameTplBusiness.preview(photoStickerFrameTplPreviewReq));
    }

    /**
     * 大头贴终端预览
     *
     * @param reqDto
     * @return
     */
    @ApiOperation("大头贴终端预览和生成")
    @PostMapping("/terminal/previewOrGen")
    @Deprecated
    public ResultData<PhotoStickerFrameTplGenVo> terminalPreview(@RequestBody @Validated PhotoStickerFrameTplTerminalPreviewGenReq reqDto) {
        return ResultData.OK(photoStickerFrameTplBusiness.terminalPreview(reqDto));
    }
}
