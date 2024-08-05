package com.ection.platform.drivermanager.db.service;


import com.ection.platform.drivermanager.model.ArchiveNumber;
import com.ection.platform.drivermanager.request.archivenumber.ArchiveNumberQueryReq;
import com.ection.platform.drivermanager.respone.ArchiveNumberVo;

import java.util.List;

/**
 * @author lsx
 * @Date 2024-07-30
 */
public interface IArchiveNumberService {

    Integer insert(ArchiveNumber archiveNumber);

    List<ArchiveNumberVo> page(ArchiveNumberQueryReq archiveNumberQueryReq);

    ArchiveNumber getById(Integer id);

    Integer edit(ArchiveNumber archiveNumber);

  List<ArchiveNumber> list(List<String> archiveNoPrefixList);
}
