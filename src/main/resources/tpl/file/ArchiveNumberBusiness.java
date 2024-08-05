package com.ection.platform.drivermanager.business;

import com.ection.platform.drivermanager.db.service.impl.ArchiveNumberService;
import com.ection.platform.drivermanager.model.ArchiveNumber;
import com.ection.platform.drivermanager.request.archivenumber.ArchiveNumberAddReq;
import com.ection.platform.drivermanager.request.archivenumber.ArchiveNumberEditReq;
import com.ection.platform.drivermanager.request.archivenumber.ArchiveNumberQueryReq;
import com.ection.platform.drivermanager.respone.ArchiveNumberVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author lsx
 * @date 2024-07-30
 */
@Service
@Slf4j
public class ArchiveNumberBusiness {

  @Resource
  private ArchiveNumberService archiveNumberService;

  public Integer add(ArchiveNumberAddReq archiveNumberAddReq) {
    ArchiveNumber archiveNumber = ArchiveNumberAddReq.toArchiveNumber(archiveNumberAddReq);
    return archiveNumberService.insert(archiveNumber);
  }

  public List<ArchiveNumberVo> page(ArchiveNumberQueryReq archiveNumberQueryReq) {
    return archiveNumberService.page(archiveNumberQueryReq);
  }

  public ArchiveNumberVo getById(Integer id) {
    ArchiveNumber archiveNumber = archiveNumberService.getById(id);
    return ArchiveNumberVo.toArchiveNumberVo(archiveNumber);
  }

  public Integer edit(ArchiveNumberEditReq archiveNumberEditReq) {
    ArchiveNumber archiveNumber = ArchiveNumberEditReq.toArchiveNumber(archiveNumberEditReq);
    return archiveNumberService.edit(archiveNumber);
  }

  public List<ArchiveNumber> list() {
    return archiveNumberService.list();
  }
}
