package com.ection.platform.drivermanager.db.service.impl;

import com.ection.platform.drivermanager.db.dao.ArchiveNumberMapper;
import com.ection.platform.drivermanager.db.service.IArchiveNumberService;
import com.ection.platform.drivermanager.model.ArchiveNumber;
import com.ection.platform.drivermanager.request.archivenumber.ArchiveNumberQueryReq;
import com.ection.platform.drivermanager.respone.ArchiveNumberVo;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author lsx
 * @Date 2024-07-30
 */
@Service
public class ArchiveNumberService implements IArchiveNumberService {

  @Resource
  private ArchiveNumberMapper archiveNumberMapper;

  @Override
  public Integer insert(ArchiveNumber archiveNumber) {
    archiveNumberMapper.insertSelective(archiveNumber);
    return archiveNumber.getId();
  }

  @Override
  public List<ArchiveNumberVo> page(ArchiveNumberQueryReq archiveNumberQueryReq) {
    return archiveNumberMapper.page(archiveNumberQueryReq);
  }

  @Override
  public ArchiveNumber getById(Integer id) {
    return archiveNumberMapper.selectByPrimaryKey(id);
  }

  @Override
  public Integer edit(ArchiveNumber archiveNumber) {
    return archiveNumberMapper.updateByPrimaryKeySelective(archiveNumber);
  }

  @Override
  public List<ArchiveNumber> list(List<String> archiveNoPrefixList) {
    Example example = new Example(ArchiveNumber.class);
    Example.Criteria criteria = example.createCriteria();
    if (ObjectUtils.isNotEmpty(archiveNoPrefixList)) {
      for (String prefix : archiveNoPrefixList) {
        criteria.orLike("archiveNoLow", prefix + "%");
        criteria.orLike("archiveNoHigh", prefix + "%");
      }
    }
    return archiveNumberMapper.selectByExample(example);
  }
}
