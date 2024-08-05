package com.ection.platform.drivermanager.db.dao;

import com.ection.platform.drivermanager.model.ArchiveNumber;
import com.ection.platform.drivermanager.request.archivenumber.ArchiveNumberQueryReq;
import com.ection.platform.drivermanager.respone.ArchiveNumberVo;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface ArchiveNumberMapper extends Mapper<ArchiveNumber> {
  List<ArchiveNumberVo> page(ArchiveNumberQueryReq archiveNumberQueryReq);
}
