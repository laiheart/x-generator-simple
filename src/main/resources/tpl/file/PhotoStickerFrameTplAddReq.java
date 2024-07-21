package tpl.file;

import com.ection.platform.common.util.JackSonUtils;
import com.ection.platform.photo.sticker.model.PhotoStickerFrameTpl;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.List;

/**
 * @author lsx
 * @date 2024-07-01
 */
@ApiModel(description = "")
@Data
public class PhotoStickerFrameTplAddReq implements Serializable {
    /**
     * 相框模板名称
     */
    @NotBlank(message = "相框模板名称不能为空")
    @Size(max = 20, message = "相框模板名称长度不得超过20")
    @ApiModelProperty("相框模板名称")
    private String name;

    /**
     * 模板图片
     */
    @ApiModelProperty("模板图片")
    private String tplImgUrl;

    /**
     * 宫格位置数组
     */
    @ApiModelProperty("宫格位置数组")
    private List<PhotoStickerPositionDto> positionList;

    /**
     * 宫格。2：二宫格，4：四宫格，8：八宫格
     */
    @ApiModelProperty("宫格。2：二宫格，4：四宫格，8：八宫格")
    private Integer type;

    /**
     * 状态。0：启用，1禁用
     */
    @ApiModelProperty("状态。0：启用，1禁用")
    @NotNull(message = "状态不能为空")
    private Integer status;

    public static PhotoStickerFrameTpl toPhotoStickerFrameTpl(PhotoStickerFrameTplAddReq photoStickerFrameTplAddReq) {
        PhotoStickerFrameTpl photoStickerFrameTpl = new PhotoStickerFrameTpl();
        BeanUtils.copyProperties(photoStickerFrameTplAddReq, photoStickerFrameTpl);
        photoStickerFrameTpl.setPosition(JackSonUtils.beanToJson(photoStickerFrameTplAddReq.getPositionList()));
        return photoStickerFrameTpl;
    }
}
