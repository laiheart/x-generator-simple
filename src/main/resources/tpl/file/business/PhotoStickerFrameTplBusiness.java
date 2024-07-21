package com.ection.platform.photo.sticker.business;

import com.ection.platform.common.dto.ResultData;
import com.ection.platform.common.exception.BusinessException;
import com.ection.platform.common.util.ImageUtil;
import com.ection.platform.common.util.ResourceUtils;
import com.ection.platform.file.RemoteFileService;
import com.ection.platform.file.protocol.DeleteFileResp;
import com.ection.platform.file.protocol.FileReq;
import com.ection.platform.file.protocol.UploadFileResp;
import com.ection.platform.photo.sticker.constant.FileBizTypeConst;
import com.ection.platform.photo.sticker.db.service.IPhotoStickerFrameTplService;
import com.ection.platform.photo.sticker.dto.photostickerframetpl.*;
import com.ection.platform.photo.sticker.utils.FileServiceUtils;
import com.ection.platform.photo.sticker.utils.ImgUtils;
import com.ection.platform.photo.sticker.utils.StickerUtils;
import com.ection.platform.photo.sticker.utils.ThrowUtils;
import com.ection.platform.photo.sticker.vo.PhotoStickerFrameTplGenVo;
import com.ection.platform.photo.sticker.vo.PhotoStickerFrameTplVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author lsx
 * @date 2024-07-01
 */
@Service
@Slf4j
public class PhotoStickerFrameTplBusiness {

    @Resource
    private RemoteFileService remoteFileService;

    @Resource
    private IPhotoStickerFrameTplService photoStickerFrameTplService;

    private final static String ALLOW_IMG_TYPE = "png";

    public Long add(PhotoStickerFrameTplAddReq photoStickerFrameTplAddReq) {
        return photoStickerFrameTplService.insert(photoStickerFrameTplAddReq);
    }

    public UploadFileResp upload(MultipartFile fileSource) {
        //文件名
        String filename = fileSource.getOriginalFilename();
        if (StringUtils.isBlank(filename)) {
            throw new BusinessException("文件名不能为空");
        }
        filename = filename.replaceAll("\\s+", "");
        String imgType = filename.substring(filename.indexOf(".") + 1);
        if (!ALLOW_IMG_TYPE.equalsIgnoreCase(imgType)) {
            throw new BusinessException("文件格式不支持");
        }
        //文件类型
        String contentType = fileSource.getContentType();
        ResultData<UploadFileResp> remoteResp = remoteFileService.upload(filename, fileSource,
                FileBizTypeConst.IMG, contentType);
        if (!remoteResp.isOk()) {
            throw new BusinessException(remoteResp.getMessage());
        }
        if (remoteResp.getData() == null) {
            throw new BusinessException("上传失败");
        }
        return remoteResp.getData();
    }

    public List<PhotoStickerFrameTplVo> page(PhotoStickerFrameTplQueryReq photoStickerFrameTplQueryReq) {
        photoStickerFrameTplQueryReq.adaptQueryDate();
        return photoStickerFrameTplService.page(photoStickerFrameTplQueryReq);
    }

    public PhotoStickerFrameTplVo getById(Long id) {
        return photoStickerFrameTplService.getById(id);
    }

    public Integer edit(PhotoStickerFrameTplEditReq photoStickerFrameTplEditReq) {
        return photoStickerFrameTplService.edit(photoStickerFrameTplEditReq);
    }

    public void deleteImg(String filepath) {
        if (StringUtils.isBlank(filepath)) {
            throw new BusinessException("文件路径不能为空");
        }
        FileReq fileReq = new FileReq();
        fileReq.setFileName(FileServiceUtils.getDelFileName(filepath, FileBizTypeConst.IMG));
        fileReq.setBuzzType(FileBizTypeConst.IMG);
        ResultData<DeleteFileResp> delete = remoteFileService.delete(fileReq);
        if (!delete.isOk()) {
            throw new BusinessException("文件服务：" + delete.getMessage());
        }
    }

    public String preview(PhotoStickerFrameTplPreviewReq reqDto) {
        //获取模板图片
        Image tplImage = ImageUtil.toImage(reqDto.getTplImgUrl());
        if (tplImage == null) {
            throw new BusinessException("模板图片不存在");
        }
        List<PhotoStickerPositionDto> positionList = reqDto.getPositionList();
        List<String> headerBase64List = reqDto.getHeaderBase64List();
        //如果没有上传头像，则使用默认头像
        if (ObjectUtils.isEmpty(headerBase64List)) {
            headerBase64List = new ArrayList<>(positionList.size());
            String base64 = ResourceUtils.getResourceAsBase64(this.getClass(), "imgTpl/头像模板.png");
            for (int i = 0; i < positionList.size(); i++) {
                headerBase64List.add(base64);
            }
        }
        List<ImgUtils.Info> infoList = Collections.synchronizedList(
                new ArrayList<>(positionList.size() + 1));
        for (PhotoStickerPositionDto positionDto : positionList) {
            int height = positionDto.getHeight();
            int width = positionDto.getWidth();
            ThrowUtils.throwIf(width <= 0, "宽度需要大于0");
            ThrowUtils.throwIf(height <= 0, "高度需要大于0");
        }
        //写入图片信息
        for (int i = 0; i < positionList.size(); i++) {
            PhotoStickerPositionDto positionDto = positionList.get(i);
            String base64 = headerBase64List.get(i);
            base64 = base64.startsWith("data:") ? base64.substring(base64.indexOf(",") + 1) : base64;
            Image headerImg = ImageUtil.toImage(base64);
            //构建图片对象
            ImgUtils.ImageInfo imageInfo = ImgUtils.infoBuilder(headerImg, true, positionDto.getX(), positionDto.getY(),
                    positionDto.getWidth(), positionDto.getHeight(), positionDto.getAngle());
            infoList.add(imageInfo);
        }
        //相框模板写入信息
        infoList.add(ImgUtils.infoBuilder(tplImage, 0, 0));
        //最终合成图片
        int width = ImgUtils.getImageWidth(tplImage);
        int height = ImgUtils.getImageHeight(tplImage);
        BufferedImage image = ImgUtils.createBufferedImage(width,
                height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics = image.createGraphics();
        graphics.setBackground(Color.WHITE);
        graphics.clearRect(0, 0, width, height);
        //设置透明
        ImgUtils.setCompositeSrcOver(graphics);
        //开始绘制最终图片
        ImgUtils.draw(graphics, infoList);
        ImgUtils.dispose(graphics);
        return ImageUtil.encodeImageToBase64(image);
    }


    public PhotoStickerFrameTplGenVo terminalPreview(PhotoStickerFrameTplTerminalPreviewGenReq reqDto) {
        PhotoStickerFrameTplVo info = photoStickerFrameTplService.getById(reqDto.getId());
        if (info == null) {
            throw new BusinessException("模板不存在");
        }
        //获取模板图片
        Image tplImage = ImageUtil.toImage(info.getTplImgUrl());
        if (tplImage == null) {
            throw new BusinessException("模板图片不存在");
        }
        //返回的对象
        PhotoStickerFrameTplGenVo vo = new PhotoStickerFrameTplGenVo();

        if (reqDto.getInitPreview()) {
            vo.setPhotoStickerFrameTplVo(info);
            return vo;
        }
        if (reqDto.getPositionList() == null) {
            throw new BusinessException("宫格位置不能为空");
        }
        List<PhotoStickerPositionDto> acList = reqDto.getPositionList();
        List<PhotoStickerPositionDto> list = info.getPositionList();
        if (ObjectUtils.isEmpty(list)) {
            throw new BusinessException("模板预设的宫格位置不能为空");
        }
        List<StickerUtils.Info> infoList = Collections.synchronizedList(
                new ArrayList<>(list.size() + 1));
        List<String> headerBase64List = reqDto.getHeaderBase64List();
        if (headerBase64List != null) {
            //写入头像信息
            for (int i = 0; i < headerBase64List.size(); i++) {
                PhotoStickerPositionDto dbPosition = list.get(i);
                PhotoStickerPositionDto acPosition = acList.get(i);
                String base64 = headerBase64List.get(i);
                base64 = base64.startsWith("data:") ? base64.substring(base64.indexOf(",") + 1) : base64;
                Image headerImg = ImageUtil.toImage(base64);
                StickerUtils.Info imageInfo = StickerUtils.infoBuilder(headerImg, dbPosition.getX(), dbPosition.getY(), dbPosition.getWidth(), dbPosition.getHeight(),
                        acPosition.getX(), acPosition.getY(), acPosition.getWidth(), acPosition.getHeight(), acPosition.getAngle());
                infoList.add(imageInfo);
            }
        }
        //最终打底图片
        int width = ImgUtils.getImageWidth(tplImage);
        int height = ImgUtils.getImageHeight(tplImage);
        BufferedImage image = ImgUtils.createBufferedImage(width,
                height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = image.createGraphics();
        //设置背景颜色
        g.setBackground(Color.WHITE);
        g.clearRect(0, 0, width, height);
        //设置透明
        ImgUtils.setCompositeSrcOver(g);
        StickerUtils.draw(g, infoList);
        //将模板图片写入
        g.drawImage(tplImage, 0, 0, null);
        StickerUtils.dispose(g);
        // 将生成的图片以及模板信息封装成返回值
        String genImgBase64 = ImageUtil.encodeImageToBase64(image);
        vo.setGeneratedImgBase64(genImgBase64);
        vo.setPhotoStickerFrameTplVo(info);
        return vo;
    }

}
